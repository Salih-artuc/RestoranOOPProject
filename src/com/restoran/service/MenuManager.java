package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.*;
import com.restoran.data.DataManager;
import java.util.StringTokenizer;

/**
 * Menü yönetimi servisi
 * Interface implementasyonu
 */
public class MenuManager implements IMenuService {
    private int nextItemId = 1;

    public MenuManager() {
        loadNextItemId();
    }

    private void loadNextItemId() {
        try {
            String content = DataManager.getAllMenuItems();
            if (!content.isEmpty()) {
                StringTokenizer lines = new StringTokenizer(content, "\n");
                int maxId = 0;
                while (lines.hasMoreTokens()) {
                    String line = lines.nextToken().trim();
                    if (line.isEmpty()) continue;
                    StringTokenizer tokens = new StringTokenizer(line, "|");
                    if (tokens.countTokens() >= 2) {
                        tokens.nextToken(); // type
                        try {
                            int id = Integer.parseInt(tokens.nextToken());
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                }
                nextItemId = maxId + 1;
            }
        } catch (FileOperationException e) {
            // İlk kullanım, nextItemId = 1 kalır
        }
    }

    public void addFood(String name, double price, String description, String category) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Ürün adı boş olamaz!");
        }
        if (price <= 0) {
            throw new InvalidInputException("Fiyat 0'dan büyük olmalıdır!");
        }
        
        Food food = new Food(nextItemId++, name, price, description, category);
        DataManager.saveMenuItem(food);
    }

    public void addDessert(String name, double price, String description, String dessertType) 
            throws InvalidInputException, FileOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Ürün adı boş olamaz!");
        }
        if (price <= 0) {
            throw new InvalidInputException("Fiyat 0'dan büyük olmalıdır!");
        }
        
        Dessert dessert = new Dessert(nextItemId++, name, price, description, dessertType);
        DataManager.saveMenuItem(dessert);
    }

    public void deleteItem(int itemId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            throw new NotFoundException("Menü boş!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 2) {
                String type = tokens.nextToken();
                try {
                    int id = Integer.parseInt(tokens.nextToken());
                    if (id != itemId) {
                        newContent.append(line).append("\n");
                    } else {
                        found = true;
                    }
                } catch (NumberFormatException e) {
                    newContent.append(line).append("\n");
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Ürün bulunamadı!");
        }
        
        DataManager.updateMenuFile(newContent.toString());
    }

    public void updatePrice(int itemId, double newPrice) 
            throws NotFoundException, InvalidInputException, FileOperationException {
        if (newPrice <= 0) {
            throw new InvalidInputException("Fiyat 0'dan büyük olmalıdır!");
        }
        
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            throw new NotFoundException("Menü boş!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            // Satırı | karakterine göre böl
            String[] parts = line.split("\\|", -1);
            
            if (parts.length >= 6) {
                try {
                    int id = Integer.parseInt(parts[1].trim());
                    if (id == itemId) {
                        // Ürün bulundu, fiyatı güncelle
                        // Format: type|id|name|price|description|isActive|category/dessertType
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(parts[0]).append("|") // type
                               .append(parts[1]).append("|") // id
                               .append(parts[2]).append("|") // name
                               .append(newPrice).append("|") // yeni fiyat
                               .append(parts[4]).append("|") // description
                               .append(parts[5]); // isActive
                        
                        // Son token category veya dessertType olabilir (varsa)
                        if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
                            newLine.append("|").append(parts[6]);
                        }
                        
                        newContent.append(newLine.toString()).append("\n");
                        found = true;
                    } else {
                        // Farklı ürün, olduğu gibi ekle
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
            throw new NotFoundException("Ürün bulunamadı!");
        }
        
        DataManager.updateMenuFile(newContent.toString());
    }

    public String displayMenu() throws FileOperationException {
        String content = DataManager.getAllMenuItems();
        if (content.isEmpty()) {
            return "Menü boş!";
        }

        StringBuilder menu = new StringBuilder();
        menu.append("=== MENÜ ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 5) {
                String type = tokens.nextToken();
                int id = Integer.parseInt(tokens.nextToken());
                String name = tokens.nextToken();
                double price = Double.parseDouble(tokens.nextToken());
                String description = tokens.nextToken();
                boolean isActive = Boolean.parseBoolean(tokens.nextToken());
                
                if (isActive) {
                    menu.append("ID: ").append(id)
                        .append(" | Tip: ").append(type)
                        .append(" | Ad: ").append(name)
                        .append(" | Fiyat: ").append(price).append(" TL")
                        .append(" | Açıklama: ").append(description);
                    
                    if (tokens.hasMoreTokens()) {
                        menu.append(" | ").append(tokens.nextToken());
                    }
                    menu.append("\n");
                }
            }
        }
        
        return menu.toString();
    }
}

