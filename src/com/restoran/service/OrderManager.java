package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.*;
import com.restoran.data.DataManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public String getActiveOrders() throws FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            return "Aktif sipariş yok!";
        }

        StringBuilder orders = new StringBuilder();
        orders.append("=== AKTİF SİPARİŞLER ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        boolean hasActive = false;
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                int id = Integer.parseInt(tokens.nextToken());
                int customerId = Integer.parseInt(tokens.nextToken());
                String customerName = tokens.nextToken();
                int tableNumber = Integer.parseInt(tokens.nextToken());
                String items = tokens.nextToken();
                double totalAmount = Double.parseDouble(tokens.nextToken());
                String statusStr = tokens.nextToken();
                
                OrderStatus status = OrderStatus.valueOf(statusStr);
                if (status != OrderStatus.SERVIS_EDILDI && status != OrderStatus.IPTAL) {
                    hasActive = true;
                    orders.append("Sipariş ID: ").append(id)
                          .append(" | Müşteri: ").append(customerName)
                          .append(" | Masa: ").append(tableNumber)
                          .append(" | Ürünler: ").append(items)
                          .append(" | Tutar: ").append(totalAmount).append(" TL")
                          .append(" | Durum: ").append(status.getDescription());
                    
                    if (tokens.countTokens() >= 2) {
                        tokens.nextToken(); // orderDate
                        int waiterId = Integer.parseInt(tokens.nextToken());
                        if (waiterId > 0 && tokens.hasMoreTokens()) {
                            orders.append(" | Garson: ").append(tokens.nextToken());
                        }
                    }
                    orders.append("\n");
                }
            }
        }
        
        if (!hasActive) {
            return "Aktif sipariş yok!";
        }
        
        return orders.toString();
    }

    public String getOrderHistory() throws FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            return "Geçmiş sipariş yok!";
        }

        StringBuilder orders = new StringBuilder();
        orders.append("=== GEÇMİŞ SİPARİŞLER ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                int id = Integer.parseInt(tokens.nextToken());
                int customerId = Integer.parseInt(tokens.nextToken());
                String customerName = tokens.nextToken();
                int tableNumber = Integer.parseInt(tokens.nextToken());
                String items = tokens.nextToken();
                double totalAmount = Double.parseDouble(tokens.nextToken());
                String statusStr = tokens.nextToken();
                
                OrderStatus status = OrderStatus.valueOf(statusStr);
                orders.append("Sipariş ID: ").append(id)
                      .append(" | Müşteri: ").append(customerName)
                      .append(" | Masa: ").append(tableNumber)
                      .append(" | Ürünler: ").append(items)
                      .append(" | Tutar: ").append(totalAmount).append(" TL")
                      .append(" | Durum: ").append(status.getDescription());
                
                if (tokens.countTokens() >= 1) {
                    String dateStr = tokens.nextToken();
                    try {
                        // Nanosaniye kısmını kaldır
                        if (dateStr.contains(".")) {
                            dateStr = dateStr.substring(0, dateStr.indexOf("."));
                        }
                        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        orders.append(" | Tarih: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                    } catch (Exception e) {
                        orders.append(" | Tarih: ").append(dateStr);
                    }
                }
                orders.append("\n");
            }
        }
        
        return orders.toString();
    }

    public String getCustomerOrders(int customerId) throws FileOperationException {
        String content = DataManager.getAllOrders();
        if (content.isEmpty()) {
            return "Sipariş geçmişiniz yok!";
        }

        StringBuilder orders = new StringBuilder();
        orders.append("=== SİPARİŞLERİM ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        boolean found = false;
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                int id = Integer.parseInt(tokens.nextToken());
                int cId = Integer.parseInt(tokens.nextToken());
                
                if (cId == customerId) {
                    found = true;
                    String customerName = tokens.nextToken();
                    int tableNumber = Integer.parseInt(tokens.nextToken());
                    String items = tokens.nextToken();
                    double totalAmount = Double.parseDouble(tokens.nextToken());
                    String statusStr = tokens.nextToken();
                    
                    OrderStatus status = OrderStatus.valueOf(statusStr);
                    orders.append("Sipariş ID: ").append(id)
                          .append(" | Masa: ").append(tableNumber)
                          .append(" | Ürünler: ").append(items)
                          .append(" | Tutar: ").append(totalAmount).append(" TL")
                          .append(" | Durum: ").append(status.getDescription());
                    
                    if (tokens.countTokens() >= 1) {
                        String dateStr = tokens.nextToken();
                        try {
                            // Nanosaniye kısmını kaldır
                            if (dateStr.contains(".")) {
                                dateStr = dateStr.substring(0, dateStr.indexOf("."));
                            }
                            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            orders.append(" | Tarih: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                        } catch (Exception e) {
                            orders.append(" | Tarih: ").append(dateStr);
                        }
                    }
                    orders.append("\n");
                }
            }
        }
        
        if (!found) {
            return "Sipariş geçmişiniz yok!";
        }
        
        return orders.toString();
    }
}

