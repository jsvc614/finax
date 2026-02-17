package com.example.finax.util;

public class CompletedFilterHelper {
    /**
     * Converts string-based completion status filter to Boolean for database queries.
     * Provides case-insensitive mapping from API parameter to query filter.
     * 
     * @param status String status filter from API request ("completed", "pending", or other)
     * @return Boolean filter: true for completed, false for pending, null for no filter
     */
    public static Boolean getCompletedFilter(String status) {
        // Map API parameter values to boolean filters for repository queries
        if ("completed".equalsIgnoreCase(status)) return Boolean.TRUE;
        if ("pending".equalsIgnoreCase(status)) return Boolean.FALSE;
        // Any other value (including null/empty) means no completion filter
        return null;
    }
}
