package com.restoran.ui;

import com.restoran.model.*;
import com.restoran.service.*;
import java.util.Scanner;

/**
 * Business interface
 */
public class BusinessInterface {
    private Scanner scanner;
    private Business business;
    private MenuManager menuManager;
    private OrderManager orderManager;
    private ReservationManager reservationManager;
    private TableManager tableManager;
    private WaiterManager waiterManager;

    public BusinessInterface(Scanner scanner, Business business) {
        this.scanner = scanner;
        this.business = business;
        this.menuManager = new MenuManager();
        this.orderManager = new OrderManager();
        this.reservationManager = new ReservationManager();
        this.tableManager = new TableManager();
        this.waiterManager = new WaiterManager();
    }

    public void start() {
        System.out.println("\n=== BUSINESS PANEL ===");
        System.out.println("Welcome " + business.getBusinessName() + "!");
        System.out.println("Manager: " + business.getFullName());
        
        showMenu();
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n=== BUSINESS MENU ===");
            System.out.println("1. Menu");
            System.out.println("2. Add Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Update Price");
            System.out.println("5. Active Orders");
            System.out.println("6. Order History");
            System.out.println("7. Update Order Status");
            System.out.println("8. Table Information");
            System.out.println("9. Reservations");
            System.out.println("10. Cancel Reservation");
            System.out.println("11. Assign Waiter");
            System.out.println("12. Add Waiter");
            System.out.println("13. Remove Waiter");
            System.out.println("14. Add Table");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        showMenuManagement();
                        break;
                    case "2":
                        addProduct();
                        break;
                    case "3":
                        deleteProduct();
                        break;
                    case "4":
                        updatePrice();
                        break;
                    case "5":
                        showActiveOrders();
                        break;
                    case "6":
                        showOrderHistory();
                        break;
                    case "7":
                        updateOrderStatus();
                        break;
                    case "8":
                        showTableInfo();
                        break;
                    case "9":
                        showReservations();
                        break;
                    case "10":
                        cancelReservation();
                        break;
                    case "11":
                        assignWaiter();
                        break;
                    case "12":
                        addWaiter();
                        break;
                    case "13":
                        removeWaiter();
                        break;
                    case "14":
                        addTable();
                        break;
                    case "0":
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showMenuManagement() {
        try {
            System.out.println("\n" + menuManager.displayMenu());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            System.out.println("\n=== ADD PRODUCT ===");
            System.out.println("1. Food");
            System.out.println("2. Dessert");
            System.out.print("Your choice: ");
            String type = scanner.nextLine().trim();
            
            System.out.print("Product name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Description: ");
            String description = scanner.nextLine().trim();
            
            if (type.equals("1")) {
                System.out.print("Category (Main dish, Soup, Salad, etc.): ");
                String category = scanner.nextLine().trim();
                menuManager.addFood(name, price, description, category);
                System.out.println("Food added!");
            } else if (type.equals("2")) {
                System.out.print("Dessert type (Milk-based, Syrupy, Ice cream, etc.): ");
                String dessertType = scanner.nextLine().trim();
                menuManager.addDessert(name, price, description, dessertType);
                System.out.println("Dessert added!");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid price!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            System.out.println("\n=== DELETE PRODUCT ===");
            System.out.println(menuManager.displayMenu());
            System.out.print("Product ID to delete: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            menuManager.deleteItem(itemId);
            System.out.println("Product deleted!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updatePrice() {
        try {
            System.out.println("\n=== UPDATE PRICE ===");
            System.out.println(menuManager.displayMenu());
            System.out.print("Product ID to update price: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("New price: ");
            double newPrice = Double.parseDouble(scanner.nextLine().trim());
            menuManager.updatePrice(itemId, newPrice);
            System.out.println("Price updated!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showActiveOrders() {
        try {
            String content = com.restoran.data.DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nNo active orders!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== ACTIVE ORDERS ===\n");
            
            String[] lines = content.split("\n");
            boolean hasActive = false;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split("\\|", -1);
                if (fields.length >= 7) {
                    int id = Integer.parseInt(fields[0]);
                    int customerId = Integer.parseInt(fields[1]);
                    String customerName = fields[2];
                    int tableNumber = Integer.parseInt(fields[3]);
                    String items = fields[4];
                    double totalAmount = Double.parseDouble(fields[5]);
                    String statusStr = fields[6];
                    
                    com.restoran.model.OrderStatus status;
                    try {
                        status = com.restoran.model.OrderStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        // Status parse edilemezse atla
                        continue;
                    }
                    
                    if (status != com.restoran.model.OrderStatus.SERVED && status != com.restoran.model.OrderStatus.CANCELLED) {
                        hasActive = true;
                        orders.append("Order ID: ").append(id)
                              .append(" | Customer: ").append(customerName)
                              .append(" | Table: ").append(tableNumber)
                              .append(" | Items: ").append(items)
                              .append(" | Amount: ").append(totalAmount).append(" TL")
                              .append(" | Status: ").append(status.getDescription());
                        
                        // Read date information
                        if (fields.length >= 8) {
                            String dateStr = fields[7]; // orderDate
                            try {
                                // Nanosaniye kısmını kaldır
                                if (dateStr.contains(".")) {
                                    dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                }
                                java.time.LocalDateTime date = java.time.LocalDateTime.parse(dateStr, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                orders.append(" | Date: ").append(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                            } catch (Exception e) {
                                // Don't show date if parsing fails
                            }
                            
                            // Read waiter information
                            if (fields.length >= 10) {
                                try {
                                    int waiterId = Integer.parseInt(fields[8]);
                                    if (waiterId > 0) {
                                        orders.append(" | Waiter: ").append(fields[9]);
                                    }
                                } catch (NumberFormatException e) {
                                    // Waiter ID parse edilemezse atla
                                }
                            }
                        }
                        orders.append("\n");
                    }
                }
            }
            
            if (!hasActive) {
                System.out.println("\nNo active orders!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showOrderHistory() {
        try {
            String content = com.restoran.data.DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nNo order history!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== PAST ORDERS ===\n");
            
            String[] lines = content.split("\n");
            boolean found = false;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split("\\|", -1);
                if (fields.length >= 7) {
                    int id = Integer.parseInt(fields[0]);
                    int customerId = Integer.parseInt(fields[1]);
                    String customerName = fields[2];
                    int tableNumber = Integer.parseInt(fields[3]);
                    String items = fields[4];
                    double totalAmount = Double.parseDouble(fields[5]);
                    String statusStr = fields[6];
                    
                    com.restoran.model.OrderStatus status;
                    try {
                        status = com.restoran.model.OrderStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        // Status parse edilemezse atla
                        continue;
                    }
                    
                    // Sadece SERVED veya CANCELLED olanları göster
                    if (status == com.restoran.model.OrderStatus.SERVED || status == com.restoran.model.OrderStatus.CANCELLED) {
                        found = true;
                        orders.append("Order ID: ").append(id)
                              .append(" | Customer: ").append(customerName)
                              .append(" | Table: ").append(tableNumber)
                              .append(" | Items: ").append(items)
                              .append(" | Amount: ").append(totalAmount).append(" TL")
                              .append(" | Status: ").append(status.getDescription());
                        
                        // Read date information
                        if (fields.length >= 8) {
                            String dateStr = fields[7];
                            try {
                                // Nanosaniye kısmını kaldır
                                if (dateStr.contains(".")) {
                                    dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                }
                                java.time.LocalDateTime date = java.time.LocalDateTime.parse(dateStr, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                orders.append(" | Tarih: ").append(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                            } catch (Exception e) {
                                // Parse edilemezse tarih gösterme
                            }
                        }
                        orders.append("\n");
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nNo order history!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showTableInfo() {
        try {
            System.out.println("\n" + tableManager.getAllTables());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        try {
            System.out.println("\n=== UPDATE ORDER STATUS ===");
            showActiveOrders();
            System.out.print("Order ID to update status: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println("\nOrder Statuses:");
            System.out.println("1. Pending");
            System.out.println("2. Preparing");
            System.out.println("3. Ready");
            System.out.println("4. Served");
            System.out.println("5. Cancelled");
            System.out.print("Select new status (1-5): ");
            String statusChoice = scanner.nextLine().trim();
            
            com.restoran.model.OrderStatus newStatus;
            switch (statusChoice) {
                case "1":
                    newStatus = com.restoran.model.OrderStatus.PENDING;
                    break;
                case "2":
                    newStatus = com.restoran.model.OrderStatus.PREPARING;
                    break;
                case "3":
                    newStatus = com.restoran.model.OrderStatus.READY;
                    break;
                case "4":
                    newStatus = com.restoran.model.OrderStatus.SERVED;
                    break;
                case "5":
                    newStatus = com.restoran.model.OrderStatus.CANCELLED;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            // Get order's table number
            int tableNumber = orderManager.getOrderTableNumber(orderId);
            
            // Update order status
            orderManager.updateOrderStatus(orderId, newStatus);
            
            // If order is SERVED or CANCELLED, clear the table
            if (newStatus == com.restoran.model.OrderStatus.SERVED || 
                newStatus == com.restoran.model.OrderStatus.CANCELLED) {
                if (tableNumber > 0) {
                    tableManager.updateTableStatus(tableNumber, false, false);
                    System.out.println("Table " + tableNumber + " cleared!");
                }
            }
            
            System.out.println("Order status updated: " + newStatus.getDescription());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showReservations() {
        try {
            System.out.println("\n" + reservationManager.getAllReservations());
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void assignWaiter() {
        try {
            System.out.println("\n=== ASSIGN WAITER ===");
            showActiveOrders();
            System.out.print("Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println(waiterManager.getAllWaiters());
            System.out.print("Waiter ID: ");
            int waiterId = Integer.parseInt(scanner.nextLine().trim());
            
            // Waiter ID check
            String waiterName = waiterManager.getWaiterInfo(waiterId);
            orderManager.assignWaiter(orderId, waiterId, waiterName);
            System.out.println("Waiter assigned!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (com.restoran.exception.NotFoundException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void addWaiter() {
        try {
            System.out.println("\n=== ADD WAITER ===");
            System.out.print("Ad: ");
            String name = scanner.nextLine().trim();
            System.out.print("Soyad: ");
            String surname = scanner.nextLine().trim();
            System.out.print("Telefon: ");
            String phone = scanner.nextLine().trim();
            
            waiterManager.addWaiter(name, surname, phone);
            System.out.println("Waiter added!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        try {
            System.out.println("\n=== CANCEL RESERVATION ===");
            System.out.println(reservationManager.getAllReservations());
            System.out.print("Reservation ID to cancel: ");
            int reservationId = Integer.parseInt(scanner.nextLine().trim());
            
            reservationManager.cancelReservation(reservationId, -1); // -1 = işletme, kontrol yapma
            System.out.println("Reservation cancelled!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeWaiter() {
        try {
            System.out.println("\n=== REMOVE WAITER ===");
            System.out.println(waiterManager.getAllWaiters());
            System.out.print("Waiter ID to remove: ");
            int waiterId = Integer.parseInt(scanner.nextLine().trim());
            
            waiterManager.removeWaiter(waiterId);
            System.out.println("Waiter removed!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addTable() {
        try {
            System.out.println("\n=== ADD TABLE ===");
            System.out.print("Kapasite: ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            
            // Automatic table number assignment
            int nextTableNumber = tableManager.getNextTableNumber();
            tableManager.addTable(nextTableNumber, capacity);
            System.out.println("Table added! Table number: " + nextTableNumber);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

