package com.restoran.model;

/**
 * Kullanıcı abstract sınıfı
 */
public abstract class User {
    protected String name;
    protected String surname;
    protected String username;
    protected String password;

    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    // Abstract method
    public abstract String getUserType();

    // Encapsulation
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}

