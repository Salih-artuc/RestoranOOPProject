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
            throw new InvalidInputException("Username cannot be empty!");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty!");
        }

        Business business = DataManager.loadBusiness(username, password);
        
        if (business == null) {
            throw new InvalidInputException("Wrong username or password!");
        }
        
        return business;
    }

    public Customer loginCustomer(String username, String password) throws InvalidInputException, FileOperationException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannnot be empty");
        }

        Customer customer = DataManager.loadCustomer(username, password);
        
        if (customer == null) {
            throw new InvalidInputException("Wrong username or password!");
        }
        
        return customer;
    }

    public Customer registerCustomer(String name, String surname, String username, String password) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty!");
        }
        
        if (surname == null || surname.trim().isEmpty()) {
            throw new InvalidInputException("Surname cannot be empty!");
        }
        
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty!");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty!");
        }

        // Kullanıcı adı kontrolü
        if (DataManager.customerExists(username)) {
            throw new InvalidInputException("This username already exists!");
        }

        // Yeni müşteri oluştur
        int customerId = DataManager.getNextCustomerId();
        Customer customer = new Customer(name, surname, username, password, customerId);
        DataManager.saveCustomer(customer);
        
        return customer;
    }
}

