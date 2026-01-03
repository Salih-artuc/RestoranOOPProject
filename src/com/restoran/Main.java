package com.restoran;

import com.restoran.ui.RestaurantApp;
import com.restoran.util.DataInitializer;

/**
 * Main class - Application entry point
 */
public class Main {
    public static void main(String[] args) {
        // Create initial data
        System.out.println("Starting system...");
        System.out.println("Ya Allah Bismillah");
        DataInitializer.initializeData();
        System.out.println("System ready!\n");
        
        RestaurantApp app = new RestaurantApp();
        app.start();
    }
}

