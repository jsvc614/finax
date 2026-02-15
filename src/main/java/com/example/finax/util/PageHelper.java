package com.example.finax.util;

import org.springframework.data.domain.Page;

import java.util.Map;

public class PageHelper {
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
