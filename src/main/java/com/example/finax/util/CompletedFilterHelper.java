package com.example.finax.util;

public class CompletedFilterHelper {
    public static Boolean getCompletedFilter(String status) {
        if ("completed".equalsIgnoreCase(status)) return Boolean.TRUE;
        if ("pending".equalsIgnoreCase(status)) return Boolean.FALSE;
        return null;
    }
}
