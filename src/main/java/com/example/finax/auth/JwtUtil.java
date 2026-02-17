package com.example.finax.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${finax.jwtSecret}")
    private String jwtSecret;

    @Value("${finax.jwtAccessTokenValidity}")
    private long jwtExpirationMs;

    /**
     * Generates a JWT access token for the given user email.
     * 
     * @param email The user's email address to be used as the token subject
     * @return A signed JWT token string containing user email and expiration info
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(accessKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Validates a JWT token and extracts the user email from it.
     * 
     * @param token The JWT token string to validate
     * @return The user email if token is valid, null if invalid or expired
     */
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey())
                    .build()
                    .parseClaimsJws(token);
            return Jwts.parserBuilder()
                    .setSigningKey(accessKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    private Key accessKey() {
        // Decode the Base64-encoded secret key and create a signing key
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}