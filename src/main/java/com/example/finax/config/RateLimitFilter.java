package com.example.finax.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements Filter {

    // Caffeine cache with a time-based eviction policy
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES) // Evict buckets 30 minutes after last access
            .build();

    /**
     * Creates a new rate limiting bucket using the Token Bucket algorithm.
     * Current configuration: 100 requests per 30 minutes with interval refill.
     *
     * @return A new Bucket4j bucket configured with the rate limiting policy
     */
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(30)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Main rate limiting filter logic applied to each HTTP request.
     * Rate limiting is applied based on the request's IP address before
     * authentication.
     *
     * @param request  The incoming HTTP request
     * @param response The HTTP response to modify if rate limit exceeded
     * @param chain    Filter chain to continue processing if rate limit not
     *                 exceeded
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Use IP address as the rate-limiting key
        String clientIp = req.getRemoteAddr();

        // Get or create a rate limiting bucket for this IP
        Bucket bucket = buckets.get(clientIp, k -> createNewBucket());

        // Try to consume 1 token from the bucket (non-blocking)
        if (bucket.tryConsume(1)) {
            // Token available - allow request to proceed
            chain.doFilter(request, response);
        } else {
            // Rate limit exceeded - return 429 Too Many Requests
            res.setStatus(429);
            res.setContentType("application/json");
            res.getWriter().write("{\"success\":false,\"message\":\"Too many requests, try again later\"}");
        }
    }
}