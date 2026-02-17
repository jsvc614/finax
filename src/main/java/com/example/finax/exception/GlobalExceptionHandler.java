package com.example.finax.exception;

import com.example.finax.dto.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Handles validation errors from @Valid annotations on request bodies.
         * Transforms Spring's MethodArgumentNotValidException into a structured error
         * response.
         *
         * @param ex The validation exception containing field-level errors
         * @return ResponseEntity with structured validation errors grouped by field
         *         name
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
                Map<String, List<String>> fieldErrors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.groupingBy(
                                FieldError::getField,
                                Collectors.mapping(
                                        fieldError -> fieldError.getDefaultMessage() != null
                                                ? fieldError.getDefaultMessage()
                                                : "",
                                        Collectors.toList())));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.of("Validation failed", fieldErrors));
        }

        /**
         * Handles ResourceNotFoundException and returns a 404 Not Found response.
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse.of("Not Found", ex.getMessage()));
        }

        /**
         * Handles UnauthorizedException and returns a 401 Unauthorized response.
         */
        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.of("Unauthorized", ex.getMessage()));
        }

        /**
         * Handles RateLimitExceededException and returns a 429 Too Many Requests
         * response.
         */
        @ExceptionHandler(RateLimitExceededException.class)
        public ResponseEntity<ErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(ErrorResponse.of("Too Many Requests", ex.getMessage()));
        }

        /**
         * Handles BadRequestException and returns a 400 Bad Request response.
         */
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ErrorResponse.of("Bad Request", ex.getMessage()));
        }

        /**
         * Handles UserAlreadyExistsException and returns a 409 Conflict response.
         */
        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ErrorResponse.of("User already exists", ex.getMessage()));
        }

        /**
         * Handles generic RuntimeException and returns a 500 Internal Server Error
         * response.
         * This is a fallback for unexpected runtime exceptions.
         */
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ErrorResponse.of("Internal Server Error", "An unexpected error occurred"));
        }

        /**
         * Handles InvalidCredentialsException and returns a 401 Unauthorized response.
         */
        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ErrorResponse.of("Invalid Credentials", ex.getMessage()));
        }
}