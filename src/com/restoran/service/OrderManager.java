package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.*;
import com.restoran.data.DataManager;
import java.util.StringTokenizer;

/**
 * Sipariş yönetimi servisi
 * Interface implementasyonu
 */
public class OrderManager implements IOrderService {
    private int nextOrderId = 1;

    public OrderManager() {
        loadNextOrderId();
    }

    private void loadNextOrderId() {
        try {
            String content = DataManager.getAllOrders();
            if (!content.isEmpty()) {
                StringTokenizer lines = new StringTokenizer(content, "\n");
                int maxId = 0;
                while (lines.hasMoreTokens()) {
                    String line = lines.nextToken().trim();
                    if (line.isEmpty()) continue;
                    StringTokenizer tokens = new StringTokenizer(line, "|");
                    if (tokens.hasMoreTokens()) {
                        try {
                            int id = Integer.parseInt(tokens.nextToken());
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                }
                nextOrderId = maxId + 1;
            }
        } catch (FileOperationException e) {
            // İlk kullanım
        }
    }

    public int createOrder(int customerId, String customerName, int tableNumber, 
                          String items, double totalAmount) throws FileOperationException {
        Order order = new Order(nextOrderId, customerId, customerName, tableNumber, items, totalAmount);
        DataManager.saveOrder(order);
        return nextOrderId++;
    }

    public void assignWaiter(int orderId, int waiterId, String waiterName) 
            throws NotFoundException, FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            throw new NotFoundException("Sipariş bulunamadı!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 9) {
                try {
                    int id = Integer.parseInt(tokens.nextToken());
                    if (id == orderId) {
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(id).append("|")
                               .append(tokens.nextToken()).append("|") // customerId
                               .append(tokens.nextToken()).append("|") // customerName
                               .append(tokens.nextToken()).append("|") // tableNumber
                               .append(tokens.nextToken()).append("|") // items
                               .append(tokens.nextToken()).append("|") // totalAmount
                               .append(tokens.nextToken()).append("|") // status
                               .append(tokens.nextToken()).append("|") // orderDate
                               .append(waiterId).append("|")
                               .append(waiterName);
                        
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
            throw new NotFoundException("Sipariş bulunamadı!");
        }
        
        DataManager.updateOrderFile(newContent.toString());
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) 
            throws NotFoundException, FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            throw new NotFoundException("Sipariş bulunamadı!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                try {
                    int id = Integer.parseInt(tokens.nextToken());
                    if (id == orderId) {
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(id).append("|")
                               .append(tokens.nextToken()).append("|") // customerId
                               .append(tokens.nextToken()).append("|") // customerName
                               .append(tokens.nextToken()).append("|") // tableNumber
                               .append(tokens.nextToken()).append("|") // items
                               .append(tokens.nextToken()).append("|") // totalAmount
                               .append(newStatus.name()).append("|");
                        
                        // Kalan tokenları ekle
                        while (tokens.hasMoreTokens()) {
                            newLine.append("|").append(tokens.nextToken());
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
            throw new NotFoundException("Sipariş bulunamadı!");
        }
        
        DataManager.updateOrderFile(newContent.toString());
    }

    public int getOrderTableNumber(int orderId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            throw new NotFoundException("Sipariş bulunamadı!");
        }

        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                try {
                    int id = Integer.parseInt(tokens.nextToken());
                    if (id == orderId) {
                        tokens.nextToken(); // customerId
                        tokens.nextToken(); // customerName
                        int tableNumber = Integer.parseInt(tokens.nextToken()); // tableNumber
                        return tableNumber;
                    }
                } catch (NumberFormatException e) {
                    // Continue
                }
            }
        }
        
        throw new NotFoundException("Sipariş bulunamadı!");
    }
}

