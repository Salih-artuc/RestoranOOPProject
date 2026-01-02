package com.restoran.model;

/**
 * İşletme sınıfı
 */
public class Business extends User {
    private String businessName;
    private String address;

    public Business(String name, String surname, String username, String password, 
                   String businessName, String address) {
        super(name, surname, username, password);
        this.businessName = businessName;
        this.address = address;
    }

    @Override
    public String getUserType() {
        return "Business";
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }
}

