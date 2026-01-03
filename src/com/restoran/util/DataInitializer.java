package com.restoran.util;

import com.restoran.exception.FileOperationException;
import com.restoran.model.*;
import com.restoran.data.DataManager;

public class DataInitializer {
    
    public static void initializeData() {
        try {
            if (!FileHandler.fileExists("business.txt")) {
                Business business = new Business("Ahmet", "Yılmaz", "restoran", "1234", 
                                                "Lezzet Restoran", "İstanbul, Kadıköy");
                DataManager.saveBusiness(business);
                System.out.println("Business account created: restoran / 1234");
            }

            if (!FileHandler.fileExists("menu.txt")) {
                Food food1 = new Food(1, "Adana Kebab", 120.0, "Spicy minced meat kebab", "Main Dish");
                Food food2 = new Food(2, "Urfa Kebab", 120.0, "Non-spicy minced meat kebab", "Main Dish");
                Food food3 = new Food(3, "Doner", 100.0, "Chicken doner", "Main Dish");
                Food food4 = new Food(4, "Lahmacun", 35.0, "Thin dough with minced meat", "Main Dish");
                Food food5 = new Food(5, "Pide", 60.0, "Cheese pide", "Main Dish");
                Food food6 = new Food(6, "Lentil Soup", 25.0, "Hot lentil soup", "Soup");
                Food food7 = new Food(7, "Ezogelin Soup", 25.0, "Soup with bulgur", "Soup");
                Food food8 = new Food(8, "Shepherd Salad", 30.0, "Fresh vegetables", "Salad");
                Food food9 = new Food(9, "Seasonal Salad", 35.0, "Mixed salad", "Salad");
                Food food10 = new Food(10, "Grilled Meatball", 110.0, "Handmade meatball", "Main Dish");

                Dessert dessert1 = new Dessert(11, "Baklava", 80.0, "Walnut baklava", "Syrupy");
                Dessert dessert2 = new Dessert(12, "Rice Pudding", 35.0, "Hot rice pudding", "Milk-based");
                Dessert dessert3 = new Dessert(13, "Kunefe", 90.0, "Hot kunefe", "Syrupy");
                Dessert dessert4 = new Dessert(14, "Ice Cream", 40.0, "Vanilla ice cream", "Ice Cream");
                Dessert dessert5 = new Dessert(15, "Kazandibi", 40.0, "Caramel pudding", "Milk-based");

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

