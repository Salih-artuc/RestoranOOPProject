package com.restoran.model;

/**
 * Sipariş durumu enum
 */
public enum OrderStatus {
    BEKLEMEDE("Beklemede"),
    HAZIRLANIYOR("Hazırlanıyor"),
    HAZIR("Hazır"),
    SERVIS_EDILDI("Servis Edildi"),
    IPTAL("İptal");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

