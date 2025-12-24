package com.restoran.model;

/**
 * Masa sınıfı
 */
public class Table {
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
    private boolean isReserved;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = false;
        this.isReserved = false;
    }

    // Encapsulation
    public int getTableNumber() {
        return tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isReserved() {
        return isReserved;
    }
}

