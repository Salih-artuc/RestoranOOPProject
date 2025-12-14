package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.model.Business;
import com.restoran.data.DataManager;

/**
 * Giriş servisi
 */
public class LoginService {
    public Business login(String username, String password) throws InvalidInputException, FileOperationException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Kullanıcı adı boş olamaz!");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Şifre boş olamaz!");
        }

        Business business = DataManager.loadBusiness(username, password);
        
        if (business == null) {
            throw new InvalidInputException("Kullanıcı adı veya şifre hatalı!");
        }
        
        return business;
    }
}

