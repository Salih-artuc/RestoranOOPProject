package com.restoran.ui;

import com.restoran.model.*;
import com.restoran.service.*;
import com.restoran.data.DataManager;
import com.restoran.exception.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Customer interface
 */
public class CustomerInterface {
    private Scanner scanner;
    private Customer customer;
    private MenuManager menuManager;
    private OrderManager orderManager;
    private ReservationManager reservationManager;
    private TableManager tableManager;

    public CustomerInterface(Scanner scanner) {
        this.scanner = scanner;
        this.menuManager = new MenuManager();
        this.orderManager = new OrderManager();
        this.reservationManager = new ReservationManager();
        this.tableManager = new TableManager();
    }

    public void start() {
        System.out.println("\n=== CUSTOMER PANEL ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleLogin() {
        try {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            LoginService loginService = new LoginService();
            this.customer = loginService.loginCustomer(username, password);
            
            if (customer != null) {
                System.out.println("\nWelcome " + customer.getFullName() + "!");
                showMenu();
            }
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void handleRegister() {
        try {
            System.out.println("\n=== REGISTER ===");
            System.out.print("First name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Last name: ");
            String surname = scanner.nextLine().trim();
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("All fields must be filled!");
                return;
            }
            
            LoginService loginService = new LoginService();
            this.customer = loginService.registerCustomer(name, surname, username, password);
            
            System.out.println("\nRegistration successful! Welcome " + customer.getFullName() + "!");
            showMenu();
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Place Order");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. My Reservations");
            System.out.println("5. Active Orders");
            System.out.println("6. Order History");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1":
                        placeOrder();
                        break;
                    case "2":
                        makeReservation();
                        break;
                    case "3":
                        cancelReservation();
                        break;
                    case "4":
                        showMyReservations();
                        break;
                    case "5":
                        showActiveOrders();
                        break;
                    case "6":
                        showOrderHistory();
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

    private void placeOrder() {
        try {
            // Show menu
            System.out.println("\n=== MENU ===");
            System.out.println(menuManager.displayMenu());
            
            // Show tables
            System.out.println("\n=== TABLES ===");
            System.out.println(tableManager.getAllTables());
            
            // Food selection
            System.out.print("\nEnter product IDs separated by commas to place order (e.g., 1,2,3): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("You must select at least one product!");
                return;
            }
            
            // Table selection
            System.out.print("Table number: ");
            int tableNumber = Integer.parseInt(scanner.nextLine().trim());
            
            // Check if table is available
            if (!tableManager.isTableAvailable(tableNumber)) {
                System.out.println("This table is not available! Please select another table.");
                return;
            }
            
            // Get menu content
            String menuContent = DataManager.getAllMenuItems();
            StringBuilder items = new StringBuilder();
            StringBuilder orderDetails = new StringBuilder();
            double totalAmount = 0.0;
            int itemCount = 0;
            
            // Process selected products
            String[] itemIds = input.split(",");
            for (String itemIdStr : itemIds) {
                try {
                    int itemId = Integer.parseInt(itemIdStr.trim());
                    boolean found = false;
                    
                    // Find product information from menu
                    String[] lines = menuContent.split("\n");
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        
                        String[] fields = line.split("\\|", -1);
                        if (fields.length >= 5) {
                            String type = fields[0];
                            int id = Integer.parseInt(fields[1]);
                            
                            if (id == itemId) {
                                found = true;
                                String name = fields[2];
                                double price = Double.parseDouble(fields[3]);
                                String description = fields[4];
                                boolean isActive = Boolean.parseBoolean(fields[5]);
                                
                                if (!isActive) {
                                    System.out.println("Product ID " + itemId + " is not active!");
                                    continue;
                                }
                                
                                totalAmount += price;
                                itemCount++;
                                
                                if (items.length() > 0) items.append(", ");
                                items.append(name);
                                
                                orderDetails.append("  - ").append(name)
                                           .append(" (").append(price).append(" TL)")
                                           .append("\n");
                                break;
                            }
                        }
                    }
                    
                    if (!found) {
                        System.out.println("Product ID " + itemId + " not found!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid product ID!");
                }
            }
            
            if (items.length() == 0) {
                System.out.println("No valid product found!");
                return;
            }
            
            // Create order
            int orderId = orderManager.createOrder(customer.getCustomerId(), 
                                                   customer.getFullName(), 
                                                   tableNumber, 
                                                   items.toString(), 
                                                   totalAmount);
            
            // Update table status (set as occupied)
            tableManager.updateTableStatus(tableNumber, true, false);
            
            // Print order information
            System.out.println("\n=== ORDER INFORMATION ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Customer: " + customer.getFullName());
            System.out.println("Table No: " + tableNumber);
            System.out.println("Items:");
            System.out.print(orderDetails.toString());
            System.out.println("Total Amount: " + totalAmount + " TL");
            System.out.println("Status: Pending");
            System.out.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            // Save order details to txt file
            saveOrderToFile(orderId, customer.getFullName(), tableNumber, items.toString(), 
                          totalAmount, orderDetails.toString());
            
            System.out.println("\nOrder created and saved successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveOrderToFile(int orderId, String customerName, int tableNumber, 
                                String items, double totalAmount, String orderDetails) {
        try {
            StringBuilder orderInfo = new StringBuilder();
            orderInfo.append("=== ORDER DETAILS ===\n");
            orderInfo.append("Order ID: ").append(orderId).append("\n");
            orderInfo.append("Customer: ").append(customerName).append("\n");
            orderInfo.append("Table No: ").append(tableNumber).append("\n");
            orderInfo.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
            orderInfo.append("Items:\n");
            orderInfo.append(orderDetails);
            orderInfo.append("Total Amount: ").append(totalAmount).append(" TL\n");
            orderInfo.append("Status: Pending\n");
            orderInfo.append("==========================================\n\n");
            
            com.restoran.util.FileHandler.writeToFile("order_details.txt", orderInfo.toString());
        } catch (Exception e) {
            System.out.println("Error occurred while saving order to file: " + e.getMessage());
        }
    }

    private void makeReservation() {
        try {
            System.out.println("\n=== RESERVATION ===");
            
            // Show tables
            System.out.println(tableManager.getAllTables());
            
            System.out.print("Your phone number: ");
            String phone = scanner.nextLine().trim();
            
            System.out.print("Table number: ");
            int tableNumber = Integer.parseInt(scanner.nextLine().trim());
            
            // Check if table is available
            if (!tableManager.isTableAvailable(tableNumber)) {
                System.out.println("This table is not available! Please select another table.");
                return;
            }
            
            // Get time only
            System.out.print("Reservation time (HH:MM format, e.g., 19:30): ");
            String timeStr = scanner.nextLine().trim();
            
            // Get today's date and combine with time
            LocalDate today = LocalDate.now();
            LocalDateTime reservationDate = LocalDateTime.parse(
                today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + timeStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            );
            
            // Check for past time
            if (reservationDate.isBefore(LocalDateTime.now())) {
                System.out.println("You cannot select a past time!");
                return;
            }
            
            // Check if reservation exists at the same time for the same table
            if (reservationManager.isReservationExists(tableNumber, reservationDate)) {
                System.out.println("This table is already reserved at this time! Please select another time.");
                return;
            }
            
            int reservationId = reservationManager.createReservation(
                customer.getCustomerId(), customer.getFullName(), phone, tableNumber, reservationDate);
            
            // Update table status (set as reserved)
            tableManager.updateTableStatus(tableNumber, false, true);
            
            System.out.println("\nReservation made!");
            System.out.println("Reservation ID: " + reservationId);
            System.out.println("Table: " + tableNumber);
            System.out.println("Date: " + reservationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Time: " + reservationDate.format(DateTimeFormatter.ofPattern("HH:mm")));
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        try {
            System.out.println("\n=== CANCEL RESERVATION ===");
            System.out.print("Reservation ID to cancel: ");
            int reservationId = Integer.parseInt(scanner.nextLine().trim());
            
            reservationManager.cancelReservation(reservationId, customer.getCustomerId());
            System.out.println("Reservation cancelled!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid ID!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showMyReservations() {
        try {
            String content = DataManager.getAllReservations();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nYou have no reservations!");
                return;
            }

            StringBuilder reservations = new StringBuilder();
            reservations.append("=== MY RESERVATIONS ===\n");
            
            String[] lines = content.split("\n");
            boolean found = false;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split("\\|", -1);
                if (fields.length >= 7) {
                    int id = Integer.parseInt(fields[0]);
                    int resCustomerId = Integer.parseInt(fields[1]);
                    
                    // Show only reservations belonging to this customer
                    if (resCustomerId == customer.getCustomerId()) {
                        String customerName = fields[2];
                        String customerPhone = fields[3];
                        int tableNumber = Integer.parseInt(fields[4]);
                        String dateStr = fields[5];
                        int numberOfGuests = Integer.parseInt(fields[6]);
                        boolean isActive = Boolean.parseBoolean(fields[7]);
                        
                        if (isActive) {
                            found = true;
                            try {
                                // Remove nanoseconds part
                                if (dateStr.contains(".")) {
                                    dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                }
                                LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                reservations.append("ID: ").append(id)
                                          .append(" | Table: ").append(tableNumber)
                                          .append(" | Phone: ").append(customerPhone)
                                          .append(" | Date: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                          .append(" | Time: ").append(date.format(DateTimeFormatter.ofPattern("HH:mm")))
                                          .append(" | Number of Guests: ").append(numberOfGuests)
                                          .append("\n");
                            } catch (Exception e) {
                                reservations.append("ID: ").append(id)
                                          .append(" | Table: ").append(tableNumber)
                                          .append(" | Date: ").append(dateStr)
                                          .append("\n");
                            }
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nYou have no active reservations!");
            } else {
                System.out.println("\n" + reservations.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showActiveOrders() {
        try {
            String content = DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nYou have no active orders!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== MY ACTIVE ORDERS ===\n");
            
            String[] lines = content.split("\n");
            boolean found = false;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split("\\|", -1);
                if (fields.length >= 7) {
                    int id = Integer.parseInt(fields[0]);
                    int cId = Integer.parseInt(fields[1]);
                    
                    if (cId == customer.getCustomerId()) {
                        String customerName = fields[2];
                        int tableNumber = Integer.parseInt(fields[3]);
                        String items = fields[4];
                        double totalAmount = Double.parseDouble(fields[5]);
                        String statusStr = fields[6];
                        
                        OrderStatus status;
                        try {
                            status = OrderStatus.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            // Status parse edilemezse atla
                            continue;
                        }
                        
                        if (status != OrderStatus.SERVED && status != OrderStatus.CANCELLED) {
                            found = true;
                            orders.append("Order ID: ").append(id)
                                  .append(" | Table: ").append(tableNumber)
                                  .append(" | Items: ").append(items)
                                  .append(" | Amount: ").append(totalAmount).append(" TL")
                                  .append(" | Status: ").append(status.getDescription());
                            
                            // Read date information
                            if (fields.length >= 8) {
                                String dateStr = fields[7];
                                try {
                                    // Remove nanoseconds part
                                    if (dateStr.contains(".")) {
                                        dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                    }
                                    LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    orders.append(" | Date: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                } catch (Exception e) {
                                    // Don't show date if parsing fails
                                }
                            }
                            orders.append("\n");
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nYou have no active orders!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showOrderHistory() {
        try {
            String content = DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nYou have no order history!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== MY ORDER HISTORY ===\n");
            
            String[] lines = content.split("\n");
            boolean found = false;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split("\\|", -1);
                if (fields.length >= 7) {
                    int id = Integer.parseInt(fields[0]);
                    int cId = Integer.parseInt(fields[1]);
                    
                    if (cId == customer.getCustomerId()) {
                        String customerName = fields[2];
                        int tableNumber = Integer.parseInt(fields[3]);
                        String items = fields[4];
                        double totalAmount = Double.parseDouble(fields[5]);
                        String statusStr = fields[6];
                        
                        OrderStatus status;
                        try {
                            status = OrderStatus.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            // Status parse edilemezse atla
                            continue;
                        }
                        
                        // Show only SERVED or CANCELLED orders
                        if (status == OrderStatus.SERVED || status == OrderStatus.CANCELLED) {
                            found = true;
                            orders.append("Order ID: ").append(id)
                                  .append(" | Table: ").append(tableNumber)
                                  .append(" | Items: ").append(items)
                                  .append(" | Amount: ").append(totalAmount).append(" TL")
                                  .append(" | Status: ").append(status.getDescription());
                            
                            // Read date information
                            if (fields.length >= 8) {
                                String dateStr = fields[7];
                                try {
                                    // Remove nanoseconds part
                                    if (dateStr.contains(".")) {
                                        dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                    }
                                    LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    orders.append(" | Date: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                } catch (Exception e) {
                                    // Don't show date if parsing fails
                                }
                            }
                            orders.append("\n");
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nYou have no order history!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
