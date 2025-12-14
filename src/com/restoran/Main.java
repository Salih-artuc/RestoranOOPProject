package com.restoran;

import com.restoran.ui.RestaurantApp;
import com.restoran.util.DataInitializer;

/**
 * Ana sınıf - Uygulama giriş noktası
 */
public class Main {
    public static void main(String[] args) {
        // Başlangıç verilerini oluştur
        System.out.println("Sistem başlatılıyor...");
        System.out.println("Ya Allah Bismillah");
        DataInitializer.initializeData();
        System.out.println("Sistem hazır!\n");
        
        RestaurantApp app = new RestaurantApp();
        app.start();
    }
}

