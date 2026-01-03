package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.*;
import com.restoran.data.DataManager;

/**
 * Order management service
 * Interface implementation
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
                String[] lines = content.split("\n");
                int maxId = 0;
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    String[] fields = line.split("\\|", -1);
                    if (fields.length > 0) {
                        try {
                            int id = Integer.parseInt(fields[0]);
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                }
                nextOrderId = maxId + 1;
            }
        } catch (FileOperationException e) {
            // First usage
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
            throw new NotFoundException("Order not found!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 9) {
                try {
                    int id = Integer.parseInt(fields[0]);
                    if (id == orderId) {
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(id).append("|")
                               .append(fields[1]).append("|") // customerId
                               .append(fields[2]).append("|") // customerName
                               .append(fields[3]).append("|") // tableNumber
                               .append(fields[4]).append("|") // items
                               .append(fields[5]).append("|") // totalAmount
                               .append(fields[6]).append("|") // status
                               .append(fields[7]).append("|") // orderDate
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
            throw new NotFoundException("Order not found!");
        }
        
        DataManager.updateOrderFile(newContent.toString());
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) 
            throws NotFoundException, FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            throw new NotFoundException("Order not found!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 7) {
                try {
                    int id = Integer.parseInt(fields[0]);
                    if (id == orderId) {
                        StringBuilder newLine = new StringBuilder();
                        newLine.append(id).append("|")
                               .append(fields[1]).append("|") // customerId
                               .append(fields[2]).append("|") // customerName
                               .append(fields[3]).append("|") // tableNumber
                               .append(fields[4]).append("|") // items
                               .append(fields[5]).append("|") // totalAmount
                               .append(newStatus.name()).append("|");
                        
                        // Add remaining fields
                        for (int i = 7; i < fields.length; i++) {
                            newLine.append("|").append(fields[i]);
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
            throw new NotFoundException("Order not found!");
        }
        
        DataManager.updateOrderFile(newContent.toString());
    }

    public int getOrderTableNumber(int orderId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            throw new NotFoundException("Order not found!");
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 7) {
                try {
                    int id = Integer.parseInt(fields[0]);
                    if (id == orderId) {
                        int tableNumber = Integer.parseInt(fields[3]); // tableNumber
                        return tableNumber;
                    }
                } catch (NumberFormatException e) {
                    // Continue
                }
            }
        }
        
        throw new NotFoundException("Order not found!");
    }
}

