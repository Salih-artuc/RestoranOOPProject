package com.restoran.model;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int customerId;
    private String customerName;
    private String customerPhone;
    private int tableNumber;
    private LocalDateTime reservationDate;
    private int numberOfGuests;
    private boolean isActive;

    public Reservation(int reservationId, int customerId, String customerName, String customerPhone, 
                      int tableNumber, LocalDateTime reservationDate, int numberOfGuests) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.tableNumber = tableNumber;
        this.reservationDate = reservationDate;
        this.numberOfGuests = numberOfGuests;
        this.isActive = true;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isActive() {
        return isActive;
    }
}

