package com.restoran.ui;

import com.restoran.exception.*;
import com.restoran.model.Business;
import com.restoran.service.LoginService;
import java.util.Scanner;

/**
 * Ana uygulama sınıfı
 * Inner class kullanımı için
 */
public class RestaurantApp {
    private Scanner scanner;
    private LoginService loginService;

    // Inner class - Uygulama yapılandırması
    public class AppConfig {
        private String appName;
        private String version;

        public AppConfig() {
            this.appName = "Restoran Yönetim Sistemi";
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
            System.out.println("Versiyon: " + version);
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
            System.out.println("\n=== GİRİŞ ===");
            System.out.println("1. Müşteri");
            System.out.println("2. İşletme");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");

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
                        System.out.println("Çıkış yapılıyor...");
                        return;
                    default:
                        System.out.println("Geçersiz seçim!");
                }
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
            }
        }
    }

    private void handleBusinessLogin() {
        try {
            System.out.println("\n=== İŞLETME GİRİŞİ ===");
            System.out.print("Kullanıcı adı: ");
            String username = scanner.nextLine().trim();
            System.out.print("Şifre: ");
            String password = scanner.nextLine().trim();

            Business business = loginService.login(username, password);
            
            if (business != null) {
                BusinessInterface businessInterface = new BusinessInterface(scanner, business);
                businessInterface.start();
            }
        } catch (InvalidInputException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("Dosya hatası: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Beklenmeyen hata: " + e.getMessage());
        }
    }
}

