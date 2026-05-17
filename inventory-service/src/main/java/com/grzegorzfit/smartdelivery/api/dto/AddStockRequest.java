package com.grzegorzfit.smartdelivery.api.dto;

import jakarta.validation.constraints.Positive;

public record AddStockRequest(

        @Positive
        int quantity
) {
}
