package com.restoran.model;

/**
 * Tatlı menüsü sınıfı
 */
public class DessertMenu extends Menu {
    public DessertMenu() {
        super("Tatlı Menüsü", "Dessert");
    }

    @Override
    public void displayMenu() {
        System.out.println("=== " + menuName + " ===");
    }
}

