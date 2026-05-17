package com.grzegorzfit.smartdelivery.api.error;

public record ApiErrorResponse(
        String error,
        String message
) {
}
