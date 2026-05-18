package com.grzegorzfit.smartdelivery.api.error;

import com.grzegorzfit.smartdelivery.application.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "PRODUCT_NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = buildValidationMessage(ex);

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "VALIDATION_ERROR",
                message
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST
        );
    }

    private static String buildValidationMessage(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "Validation failed";
        }
        return message;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "CONFLICT",
                ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException() {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected internal error occurred."
        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
