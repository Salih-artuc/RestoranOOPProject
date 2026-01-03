package com.restoran.ui;

import com.restoran.exception.*;
import com.restoran.model.Business;
import com.restoran.service.LoginService;
import java.util.Scanner;

/**
 * Main application class
 * Uses inner class
 */
public class RestaurantApp {
    private Scanner scanner;
    private LoginService loginService;

    // Inner class - Application configuration
    public class AppConfig {
        private String appName;
        private String version;

        public AppConfig() {
            this.appName = "Restaurant Management System";
            this.version = "1.0";
        }

        public String getAppName() {
            return appName;
        }

        public String getVersion() {
            return version;
        }

        public void displayWelcome() {
            System.out.println("=== " + appName + " ===");
            System.out.println("Version: " + version);
        }
    }

    public RestaurantApp() {
        this.scanner = new Scanner(System.in);
        this.loginService = new LoginService();
    }

    public void start() {
        AppConfig config = new AppConfig();
        config.displayWelcome();

        while (true) {
            System.out.println("\n=== LOGIN ===");
            System.out.println("1. Customer");
            System.out.println("2. Business");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        CustomerInterface customerInterface = new CustomerInterface(scanner);
                        customerInterface.start();
                        break;
                    case "2":
                        handleBusinessLogin();
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

    private void handleBusinessLogin() {
        try {
            System.out.println("\n=== BUSINESS LOGIN ===");
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            Business business = loginService.loginBusiness(username, password);
            
            if (business != null) {
                BusinessInterface businessInterface = new BusinessInterface(scanner, business);
                businessInterface.start();
            }
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}

