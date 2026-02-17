package com.example.finax.util;

import org.springframework.data.domain.Page;

import java.util.Map;

public class PageHelper {
    /**
     * Converts Spring Data Page object to a Map structure suitable for API responses.
     * Transforms pagination metadata and content into a standardized format.
     * 
     * @param page The Spring Data Page object containing paginated results
     * @return Map with structured pagination data including content and metadata
     */
    public static Map<String, Object> toMap(Page<?> page) {
        return Map.of(
                "todos", page.getContent(),
                "page", page.getNumber() + 1,
                "size", page.getSize(),
                "totalElements", page.getTotalElements(),
                "totalPages", page.getTotalPages()
        );
    }
}
