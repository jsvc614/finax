package com.example.finax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private boolean success = true;
    private T data;
    private String message;

    public static <T> SuccessResponse<T> of(T data, String message) {
        return new SuccessResponse<>(true, data, message);
    }
}