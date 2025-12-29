package com.restoran.model;

/**
 * Sipariş durumu enum
 */
public enum OrderStatus {
    PENDING("Pending"),
    PREPARING("Preparing"),
    READY("Ready"),
    SERVED("Served"),
    CANCELLED("Cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

