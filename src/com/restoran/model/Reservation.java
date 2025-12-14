package com.restoran.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Rezervasyon sınıfı
 */
public class Reservation {
    private int reservationId;
    private String customerName;
    private String customerPhone;
    private int tableNumber;
    private LocalDateTime reservationDate;
    private int numberOfGuests;
    private boolean isActive;

    public Reservation(int reservationId, String customerName, String customerPhone, 
                      int tableNumber, LocalDateTime reservationDate, int numberOfGuests) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.tableNumber = tableNumber;
        this.reservationDate = reservationDate;
        this.numberOfGuests = numberOfGuests;
        this.isActive = true;
    }

    // Encapsulation
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return reservationDate.format(formatter);
    }
}

