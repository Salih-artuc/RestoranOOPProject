package com.restoran.model;

public class Food extends MenuItem {
    private String category;

    public Food(int itemId, String name, double price, String description, String category) {
        super(itemId, name, price, description);
        this.category = category;
    }

    @Override
    public String getItemType() {
        return "Food";
    }

    public String getCategory() {
        return category;
    }
}

