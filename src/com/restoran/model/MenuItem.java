package com.restoran.model;

/**
 * Menü öğesi abstract sınıfı
 */
public abstract class MenuItem {
    protected int itemId;
    protected String name;
    protected double price;
    protected String description;
    protected boolean isActive;

    public MenuItem(int itemId, String name, double price, String description) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isActive = true;
    }

    // Abstract method
    public abstract String getItemType();

    // Encapsulation
    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }
}

