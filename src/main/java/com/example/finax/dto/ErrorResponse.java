package com.example.finax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private boolean success = false;
    private String error;
    private Map<String, List<String>> errors;

    public static ErrorResponse of(String errorSummary, Map<String, List<String>> fieldErrors) {
        return new ErrorResponse(false, errorSummary, fieldErrors);
    }
    public static ErrorResponse of(String errorSummary, String detailedMessage) {
        return new ErrorResponse(false, errorSummary,
                Collections.singletonMap("title", Collections.singletonList(detailedMessage)));
    }
}