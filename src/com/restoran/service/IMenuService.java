package com.restoran.service;

public interface IMenuService {
    void addFood(String name, double price, String description, String category) throws Exception;
    void addDessert(String name, double price, String description, String dessertType) throws Exception;
    void deleteItem(int itemId) throws Exception;
    void updatePrice(int itemId, double newPrice) throws Exception;
    String displayMenu() throws Exception;
}

