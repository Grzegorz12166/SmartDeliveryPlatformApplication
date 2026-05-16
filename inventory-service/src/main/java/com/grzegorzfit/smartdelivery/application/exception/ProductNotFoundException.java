package com.grzegorzfit.smartdelivery.application.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productNotFound) {
        super(productNotFound);
    }
}
