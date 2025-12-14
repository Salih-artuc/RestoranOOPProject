package com.restoran.util;

import com.restoran.exception.InvalidInputException;

/**
 * Giriş doğrulama utility sınıfı
 */
public class InputValidator {
    public static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " boş olamaz!");
        }
    }

    public static void validatePositive(double value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " 0'dan büyük olmalıdır!");
        }
    }

    public static void validatePositive(int value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " 0'dan büyük olmalıdır!");
        }
    }
}

