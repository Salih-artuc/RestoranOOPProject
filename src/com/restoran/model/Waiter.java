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

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}

