package com.restoran.exception;

/**
 * Dosya işlemleri exception'ı
 */
public class FileOperationException extends CustomException {
    public FileOperationException(String message) {
        super(message);
    }
}

