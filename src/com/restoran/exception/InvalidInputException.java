package com.restoran.exception;

/**
 * Geçersiz giriş exception'ı
 */
public class InvalidInputException extends CustomException {
    public InvalidInputException(String message) {
        super(message);
    }
}

