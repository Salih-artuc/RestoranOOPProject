package com.restoran.model;

/**
 * Yemek sınıfı
 */
public class Food extends MenuItem {
    private String category; // Ana yemek, çorba, salata vb.

    public Food(int itemId, String name, double price, String description, String category) {
        super(itemId, name, price, description);
        this.category = category;
    }

    @Override
    public String getItemType() {
        return "Yemek";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

