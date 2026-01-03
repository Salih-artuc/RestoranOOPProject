package com.restoran.model;

public class Dessert extends MenuItem {
    private String dessertType;

    public Dessert(int itemId, String name, double price, String description, String dessertType) {
        super(itemId, name, price, description);
        this.dessertType = dessertType;
    }

    @Override
    public String getItemType() {
        return "Dessert";
    }

    public String getDessertType() {
        return dessertType;
    }
}

