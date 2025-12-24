package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.model.Business;
import com.restoran.model.Customer;
import com.restoran.data.DataManager;

/**
 * Giriş servisi
 */
public class LoginService {
    public Business loginBusiness(String username, String password) throws InvalidInputException, FileOperationException {
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

    public Customer loginCustomer(String username, String password) throws InvalidInputException, FileOperationException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Kullanıcı adı boş olamaz!");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Şifre boş olamaz!");
        }

        Customer customer = DataManager.loadCustomer(username, password);
        
        if (customer == null) {
            throw new InvalidInputException("Kullanıcı adı veya şifre hatalı!");
        }
        
        return customer;
    }

    public Customer registerCustomer(String name, String surname, String username, String password) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Ad boş olamaz!");
        }
        
        if (surname == null || surname.trim().isEmpty()) {
            throw new InvalidInputException("Soyad boş olamaz!");
        }
        
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Kullanıcı adı boş olamaz!");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Şifre boş olamaz!");
        }

        // Kullanıcı adı kontrolü
        if (DataManager.customerExists(username)) {
            throw new InvalidInputException("Bu kullanıcı adı zaten kullanılıyor!");
        }

        // Yeni müşteri oluştur
        int customerId = DataManager.getNextCustomerId();
        Customer customer = new Customer(name, surname, username, password, customerId);
        DataManager.saveCustomer(customer);
        
        return customer;
    }
}

