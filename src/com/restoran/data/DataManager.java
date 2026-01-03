package com.restoran.data;

import com.restoran.exception.FileOperationException;
import com.restoran.model.*;
import com.restoran.util.FileHandler;
import java.time.format.DateTimeFormatter;

/**
 * Class for data management
 * Works with String and StringBuilder without using ArrayList
 */
public class DataManager {
    private static final String BUSINESS_FILE = "business.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";
    private static final String TABLES_FILE = "tables.txt";
    private static final String WAITERS_FILE = "waiters.txt";

    // Business operations
    public static void saveBusiness(Business business) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(business.getName()).append("|")
          .append(business.getSurname()).append("|")
          .append(business.getUsername()).append("|")
          .append(business.getPassword()).append("|")
          .append(business.getBusinessName()).append("|")
          .append(business.getAddress());
        FileHandler.writeToFile(BUSINESS_FILE, sb.toString());
    }

    public static Business loadBusiness(String username, String password) throws FileOperationException {
        String content = FileHandler.readFromFile(BUSINESS_FILE);
        if (content.isEmpty()) return null;

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 6) {
                String name = fields[0];
                String surname = fields[1];
                String uname = fields[2];
                String pwd = fields[3];
                String businessName = fields[4];
                String address = fields[5];
                
                if (uname.equals(username) && pwd.equals(password)) {
                    return new Business(name, surname, uname, pwd, businessName, address);
                }
            }
        }
        return null;
    }

    // Menu operations
    public static void saveMenuItem(MenuItem item) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getItemType()).append("|")
          .append(item.getItemId()).append("|")
          .append(item.getName()).append("|")
          .append(item.getPrice()).append("|")
          .append(item.getDescription()).append("|")
          .append(item.isActive());
        
        if (item instanceof Food) {
            sb.append("|").append(((Food) item).getCategory());
        } else if (item instanceof Dessert) {
            sb.append("|").append(((Dessert) item).getDessertType());
        }
        
        FileHandler.writeToFile(MENU_FILE, sb.toString());
    }

    public static String getAllMenuItems() throws FileOperationException {
        return FileHandler.readFromFile(MENU_FILE);
    }

    public static void updateMenuFile(String content) throws FileOperationException {
        FileHandler.overwriteFile(MENU_FILE, content);
    }

    // Order operations
    public static void saveOrder(Order order) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(order.getOrderId()).append("|")
          .append(order.getCustomerId()).append("|")
          .append(order.getCustomerName()).append("|")
          .append(order.getTableNumber()).append("|")
          .append(order.getItems()).append("|")
          .append(order.getTotalAmount()).append("|")
          .append(order.getStatus().name()).append("|")
          .append(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("|")
          .append(order.getWaiterId()).append("|")
          .append(order.getWaiterName());
        FileHandler.writeToFile(ORDERS_FILE, sb.toString());
    }

    public static String getAllOrders() throws FileOperationException {
        return FileHandler.readFromFile(ORDERS_FILE);
    }

    // Reservation operations
    public static void saveReservation(Reservation reservation) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(reservation.getReservationId()).append("|")
          .append(reservation.getCustomerId()).append("|")
          .append(reservation.getCustomerName()).append("|")
          .append(reservation.getCustomerPhone()).append("|")
          .append(reservation.getTableNumber()).append("|")
          .append(reservation.getReservationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("|")
          .append(reservation.getNumberOfGuests()).append("|")
          .append(reservation.isActive());
        FileHandler.writeToFile(RESERVATIONS_FILE, sb.toString());
    }

    public static String getAllReservations() throws FileOperationException {
        return FileHandler.readFromFile(RESERVATIONS_FILE);
    }

    // Table operations
    public static void saveTable(Table table) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(table.getTableNumber()).append("|")
          .append(table.getCapacity()).append("|")
          .append(table.isOccupied()).append("|")
          .append(table.isReserved());
        FileHandler.writeToFile(TABLES_FILE, sb.toString());
    }

    public static String getAllTables() throws FileOperationException {
        return FileHandler.readFromFile(TABLES_FILE);
    }

    // Waiter operations
    public static void saveWaiter(Waiter waiter) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(waiter.getWaiterId()).append("|")
          .append(waiter.getName()).append("|")
          .append(waiter.getSurname()).append("|")
          .append(waiter.getPhoneNumber()).append("|")
          .append(waiter.isAvailable());
        FileHandler.writeToFile(WAITERS_FILE, sb.toString());
    }

    public static String getAllWaiters() throws FileOperationException {
        return FileHandler.readFromFile(WAITERS_FILE);
    }

    public static void updateOrderFile(String content) throws FileOperationException {
        FileHandler.overwriteFile(ORDERS_FILE, content);
    }

    public static void updateTableFile(String content) throws FileOperationException {
        FileHandler.overwriteFile(TABLES_FILE, content);
    }

    public static void updateReservationFile(String content) throws FileOperationException {
        FileHandler.overwriteFile(RESERVATIONS_FILE, content);
    }

    public static void updateWaiterFile(String content) throws FileOperationException {
        FileHandler.overwriteFile(WAITERS_FILE, content);
    }

    // Customer operations
    public static void saveCustomer(Customer customer) throws FileOperationException {
        StringBuilder sb = new StringBuilder();
        sb.append(customer.getCustomerId()).append("|")
          .append(customer.getName()).append("|")
          .append(customer.getSurname()).append("|")
          .append(customer.getUsername()).append("|")
          .append(customer.getPassword());
        FileHandler.writeToFile(CUSTOMERS_FILE, sb.toString());
    }

    public static Customer loadCustomer(String username, String password) throws FileOperationException {
        String content = FileHandler.readFromFile(CUSTOMERS_FILE);
        if (content.isEmpty()) return null;

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                int customerId = Integer.parseInt(fields[0]);
                String name = fields[1];
                String surname = fields[2];
                String uname = fields[3];
                String pwd = fields[4];
                
                if (uname.equals(username) && pwd.equals(password)) {
                    return new Customer(name, surname, uname, pwd, customerId);
                }
            }
        }
        return null;
    }

    public static boolean customerExists(String username) throws FileOperationException {
        String content = FileHandler.readFromFile(CUSTOMERS_FILE);
        if (content.isEmpty()) return false;

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                String uname = fields[3];
                
                if (uname.equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getNextCustomerId() throws FileOperationException {
        String content = FileHandler.readFromFile(CUSTOMERS_FILE);
        if (content.isEmpty()) return 1;

        int maxId = 0;
        String[] lines = content.split("\n");
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
        return maxId + 1;
    }
}

