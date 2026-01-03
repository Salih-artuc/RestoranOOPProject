package com.restoran;

import com.restoran.ui.RestaurantApp;
import com.restoran.util.DataInitializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting system...");

        DataInitializer.initializeData();
        System.out.println("System ready!\n");
        
        RestaurantApp app = new RestaurantApp();
        app.start();
    }
}

