package com.restoran.util;

import com.restoran.exception.FileOperationException;
import com.restoran.model.*;
import com.restoran.data.DataManager;

/**
 * Class that creates initial data
 */
public class DataInitializer {
    
    public static void initializeData() {
        try {
            // Create business account
            if (!FileHandler.fileExists("business.txt")) {
                Business business = new Business("Ahmet", "Yılmaz", "restoran", "1234", 
                                                "Lezzet Restoran", "İstanbul, Kadıköy");
                DataManager.saveBusiness(business);
                System.out.println("Business account created: restoran / 1234");
            }

            // Create menu
            if (!FileHandler.fileExists("menu.txt")) {
                // Foods
                Food food1 = new Food(1, "Adana Kebap", 120.0, "Acılı kıyma kebabı", "Ana Yemek");
                Food food2 = new Food(2, "Urfa Kebap", 120.0, "Acısız kıyma kebabı", "Ana Yemek");
                Food food3 = new Food(3, "Döner", 100.0, "Tavuk döner", "Ana Yemek");
                Food food4 = new Food(4, "Lahmacun", 35.0, "İnce hamur üzerine kıymalı", "Ana Yemek");
                Food food5 = new Food(5, "Pide", 60.0, "Kaşarlı pide", "Ana Yemek");
                Food food6 = new Food(6, "Mercimek Çorbası", 25.0, "Sıcak mercimek çorbası", "Çorba");
                Food food7 = new Food(7, "Ezogelin Çorbası", 25.0, "Bulgurlu çorba", "Çorba");
                Food food8 = new Food(8, "Çoban Salata", 30.0, "Taze sebzeler", "Salata");
                Food food9 = new Food(9, "Mevsim Salatası", 35.0, "Karışık salata", "Salata");
                Food food10 = new Food(10, "Izgara Köfte", 110.0, "El yapımı köfte", "Ana Yemek");

                // Desserts
                Dessert dessert1 = new Dessert(11, "Baklava", 80.0, "Cevizli baklava", "Şerbetli");
                Dessert dessert2 = new Dessert(12, "Sütlaç", 35.0, "Sıcak sütlaç", "Sütlü");
                Dessert dessert3 = new Dessert(13, "Künefe", 90.0, "Sıcak künefe", "Şerbetli");
                Dessert dessert4 = new Dessert(14, "Dondurma", 40.0, "Vanilyalı dondurma", "Dondurma");
                Dessert dessert5 = new Dessert(15, "Kazandibi", 40.0, "Karamelli muhallebi", "Sütlü");

                DataManager.saveMenuItem(food1);
                DataManager.saveMenuItem(food2);
                DataManager.saveMenuItem(food3);
                DataManager.saveMenuItem(food4);
                DataManager.saveMenuItem(food5);
                DataManager.saveMenuItem(food6);
                DataManager.saveMenuItem(food7);
                DataManager.saveMenuItem(food8);
                DataManager.saveMenuItem(food9);
                DataManager.saveMenuItem(food10);
                DataManager.saveMenuItem(dessert1);
                DataManager.saveMenuItem(dessert2);
                DataManager.saveMenuItem(dessert3);
                DataManager.saveMenuItem(dessert4);
                DataManager.saveMenuItem(dessert5);

                System.out.println("Menu created (15 items)");
            }

            // Create tables
            if (!FileHandler.fileExists("tables.txt")) {
                Table table1 = new Table(1, 4);
                Table table2 = new Table(2, 4);
                Table table3 = new Table(3, 6);
                Table table4 = new Table(4, 2);
                Table table5 = new Table(5, 4);
                Table table6 = new Table(6, 8);
                Table table7 = new Table(7, 4);
                Table table8 = new Table(8, 6);

                DataManager.saveTable(table1);
                DataManager.saveTable(table2);
                DataManager.saveTable(table3);
                DataManager.saveTable(table4);
                DataManager.saveTable(table5);
                DataManager.saveTable(table6);
                DataManager.saveTable(table7);
                DataManager.saveTable(table8);

                System.out.println("Tables created (8 tables)");
            }

            // Create waiters
            if (!FileHandler.fileExists("waiters.txt")) {
                Waiter waiter1 = new Waiter(1, "Mehmet", "Demir", "05551234567");
                Waiter waiter2 = new Waiter(2, "Ayşe", "Kaya", "05559876543");
                Waiter waiter3 = new Waiter(3, "Ali", "Çelik", "05551112233");

                DataManager.saveWaiter(waiter1);
                DataManager.saveWaiter(waiter2);
                DataManager.saveWaiter(waiter3);

                System.out.println("Waiters created (3 waiters)");
            }

        } catch (FileOperationException e) {
            System.err.println("Data creation error: " + e.getMessage());
        }
    }
}

