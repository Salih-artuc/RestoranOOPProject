package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.*;
import com.restoran.data.DataManager;

public class MenuManager implements IMenuService {
    private int nextItemId = 1;

    public MenuManager() {
        loadNextItemId();
    }

    private void loadNextItemId() {
        try {
            String content = DataManager.getAllMenuItems();
            if (!content.isEmpty()) {
                String[] lines = content.split("\n");
                int maxId = 0;
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    String[] fields = line.split("\\|", -1);
                    if (fields.length >= 2) {
                        try {
                            int id = Integer.parseInt(fields[1]);
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                nextItemId = maxId + 1;
            }
        } catch (FileOperationException e) {
        }
    }

    public void addFood(String name, double price, String description, String category) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Product name cannot be empty!");
        }
        if (price <= 0) {
            throw new InvalidInputException("Price must be greater than 0!");
        }
        
        Food food = new Food(nextItemId++, name, price, description, category);
        DataManager.saveMenuItem(food);
    }

    public void addDessert(String name, double price, String description, String dessertType) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Product name cannot be empty!");
        }
        if (price <= 0) {
            throw new InvalidInputException("Price must be greater than 0!");
        }
        
        Dessert dessert = new Dessert(nextItemId++, name, price, description, dessertType);
        DataManager.saveMenuItem(dessert);
    }

    public void deleteItem(int itemId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            throw new NotFoundException("Menu is empty!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split("\\|", -1);
            
            if (parts.length >= 6) {
                try {
                    int id = Integer.parseInt(parts[1].trim());
                    
                    if (id == itemId) {
                        found = true;
                    } else if (id > itemId) {
                        int newId = id - 1;
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(parts[0]).append("|")
                               .append(newId).append("|")
                               .append(parts[2]).append("|")
                               .append(parts[3]).append("|")
                               .append(parts[4]).append("|")
                               .append(parts[5]);
                        if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
                            newLine.append("|").append(parts[6]);
                        }
                        
                        newContent.append(newLine.toString()).append("\n");
                    } else {
                        newContent.append(line).append("\n");
                    }
                } catch (NumberFormatException e) {
                    newContent.append(line).append("\n");
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Couldn't find the product!");
        }
        
        DataManager.updateMenuFile(newContent.toString());
        
        if (nextItemId > 1) {
            nextItemId--;
        }
    }

    public void updatePrice(int itemId, double newPrice) 
            throws NotFoundException, InvalidInputException, FileOperationException {
        if (newPrice <= 0) {
            throw new InvalidInputException("Price must be greater than 0!");
        }
        
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            throw new NotFoundException("Menu is empty!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split("\\|", -1);
            
            if (parts.length >= 6) {
                try {
                    int id = Integer.parseInt(parts[1].trim());
                    if (id == itemId) {
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(parts[0]).append("|")
                               .append(parts[1]).append("|")
                               .append(parts[2]).append("|")
                               .append(newPrice).append("|")
                               .append(parts[4]).append("|")
                               .append(parts[5]);
                        if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
                            newLine.append("|").append(parts[6]);
                        }
                        
                        newContent.append(newLine.toString()).append("\n");
                        found = true;
                    } else {
                        newContent.append(line).append("\n");
                    }
                } catch (NumberFormatException e) {
                    newContent.append(line).append("\n");
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Couldn't find the product!");
        }
        
        DataManager.updateMenuFile(newContent.toString());
    }

    public String displayMenu() throws FileOperationException {
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            return "Menu is empty!";
        }

        StringBuilder menu = new StringBuilder();
        menu.append("=== MENU ===\n");
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                String type = fields[0];
                int id = Integer.parseInt(fields[1]);
                String name = fields[2];
                double price = Double.parseDouble(fields[3]);
                String description = fields[4];
                boolean isActive = Boolean.parseBoolean(fields[5]);
                
                if (isActive) {
                    menu.append("ID: ").append(id)
                        .append(" | Type: ").append(type)
                        .append(" | Name: ").append(name)
                        .append(" | Price: ").append(price).append(" TL")
                        .append(" | Description: ").append(description);
                    
                    if (fields.length >= 7) {
                        menu.append(" | ").append(fields[6]);
                    }
                    menu.append("\n");
                }
            }
        }
        
        return menu.toString();
    }
}

