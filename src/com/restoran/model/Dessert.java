package com.restoran.model;

/**
 * Tatlı sınıfı
 */
public class Dessert extends MenuItem {
    private String dessertType; // Sütlü, şerbetli, dondurma vb.

    public Dessert(int itemId, String name, double price, String description, String dessertType) {
        super(itemId, name, price, description);
        this.dessertType = dessertType;
    }

    @Override
    public String getItemType() {
        return "Tatlı";
    }

    public String getDessertType() {
        return dessertType;
    }

    public void setDessertType(String dessertType) {
        this.dessertType = dessertType;
    }
}

