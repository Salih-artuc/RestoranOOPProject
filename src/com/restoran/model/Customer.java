package com.restoran.model;

/**
 * Müşteri sınıfı
 */
public class Customer extends User {
    private int customerId;
    private static int customerCounter = 1;

    public Customer(String name, String surname, String username, String password) {
        super(name, surname, username, password);
        this.customerId = customerCounter++;
    }

    public Customer(String name, String surname, String username, String password, int customerId) {
        super(name, surname, username, password);
        this.customerId = customerId;
    }

    @Override
    public String getUserType() {
        return "Customer";
    }

    public int getCustomerId() {
        return customerId;
    }
}

