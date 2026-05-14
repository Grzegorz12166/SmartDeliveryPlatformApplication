package model;

public record DeliveryAddress(
        String street,
        String city,
        String postalCode,
        String country
) {
}

