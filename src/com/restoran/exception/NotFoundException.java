package com.restoran.exception;

/**
 * Bulunamadı exception'ı
 */
public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message);
    }
}

