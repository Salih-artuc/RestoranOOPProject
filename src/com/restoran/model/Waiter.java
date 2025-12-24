package com.restoran.model;

/**
 * Garson sınıfı
 */
public class Waiter {
    private int waiterId;
    private String name;
    private String surname;
    private String phoneNumber;
    private boolean isAvailable;

    public Waiter(int waiterId, String name, String surname, String phoneNumber) {
        this.waiterId = waiterId;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.isAvailable = true;
    }

    // Encapsulation
    public int getWaiterId() {
        return waiterId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}

