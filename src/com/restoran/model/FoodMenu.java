package com.restoran.model;

/**
 * Yemek menüsü sınıfı
 */
public class FoodMenu extends Menu {
    public FoodMenu() {
        super("Yemek Menüsü", "Food");
    }

    @Override
    public void displayMenu() {
        System.out.println("=== " + menuName + " ===");
    }
}

