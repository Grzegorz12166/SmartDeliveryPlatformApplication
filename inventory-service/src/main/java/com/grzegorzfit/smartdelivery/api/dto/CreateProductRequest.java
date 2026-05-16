package com.grzegorzfit.smartdelivery.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import model.Currency;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank
        @Size(max = 100)
        String sku,

        @NotBlank
        String name,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal price,

        @NotNull
        Currency currency,

        boolean active,

        @PositiveOrZero
        int initialQuantity
) {
}
