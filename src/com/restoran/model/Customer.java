package com.restoran.model;

/**
 * Müşteri sınıfı
 */
public class Customer extends User {
    private int customerId;
    private static int customerCounter = 1;

    public Customer(String name, String surname) {
        super(name, surname, "", "");
        this.customerId = customerCounter++;
    }

    public Customer(String name, String surname, int customerId) {
        super(name, surname, "", "");
        this.customerId = customerId;
    }

    @Override
    public String getUserType() {
        return "Müşteri";
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}

