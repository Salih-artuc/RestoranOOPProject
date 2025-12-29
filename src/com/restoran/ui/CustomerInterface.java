package com.restoran.ui;

import com.restoran.model.*;
import com.restoran.service.*;
import com.restoran.data.DataManager;
import com.restoran.exception.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

/**
 * Müşteri arayüzü
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
        System.out.println("\n=== MÜŞTERİ PANELİ ===");
        System.out.println("1. Giriş Yap");
        System.out.println("2. Kayıt Ol");
        System.out.print("Seçiminiz: ");
        
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
                    System.out.println("Geçersiz seçim!");
                    return;
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void handleLogin() {
        try {
            System.out.println("\n=== GİRİŞ ===");
            System.out.print("Kullanıcı adı: ");
            String username = scanner.nextLine().trim();
            System.out.print("Şifre: ");
            String password = scanner.nextLine().trim();
            
            LoginService loginService = new LoginService();
            this.customer = loginService.loginCustomer(username, password);
            
            if (customer != null) {
                System.out.println("\nHoş geldiniz " + customer.getFullName() + "!");
                showMenu();
            }
        } catch (InvalidInputException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("Dosya hatası: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Beklenmeyen hata: " + e.getMessage());
        }
    }

    private void handleRegister() {
        try {
            System.out.println("\n=== KAYIT OL ===");
            System.out.print("Adınız: ");
            String name = scanner.nextLine().trim();
            System.out.print("Soyadınız: ");
            String surname = scanner.nextLine().trim();
            System.out.print("Kullanıcı adı: ");
            String username = scanner.nextLine().trim();
            System.out.print("Şifre: ");
            String password = scanner.nextLine().trim();
            
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("Tüm alanlar doldurulmalıdır!");
                return;
            }
            
            LoginService loginService = new LoginService();
            this.customer = loginService.registerCustomer(name, surname, username, password);
            
            System.out.println("\nKayıt başarılı! Hoş geldiniz " + customer.getFullName() + "!");
            showMenu();
        } catch (InvalidInputException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (FileOperationException e) {
            System.out.println("Dosya hatası: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Beklenmeyen hata: " + e.getMessage());
        }
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n=== MÜŞTERİ MENÜSÜ ===");
            System.out.println("1. Sipariş Ver");
            System.out.println("2. Rezervasyon Yap");
            System.out.println("3. Rezervasyon İptal");
            System.out.println("4. Rezervasyonlarım");
            System.out.println("5. Aktif Siparişler");
            System.out.println("6. Geçmiş Siparişler");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            
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

    private void placeOrder() {
        try {
            // Menüyü göster
            System.out.println("\n=== MENÜ ===");
            System.out.println(menuManager.displayMenu());
            
            // Masaları göster
            System.out.println("\n=== MASALAR ===");
            System.out.println(tableManager.getAllTables());
            
            // Yemek seçimi
            System.out.print("\nSipariş vermek için ürün ID'lerini virgülle ayırarak giriniz (örn: 1,2,3): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("En az bir ürün seçmelisiniz!");
                return;
            }
            
            // Masa seçimi
            System.out.print("Masa numarası: ");
            int tableNumber = Integer.parseInt(scanner.nextLine().trim());
            
            // Masa müsait mi kontrol et
            if (!tableManager.isTableAvailable(tableNumber)) {
                System.out.println("Bu masa müsait değil! Lütfen başka bir masa seçiniz.");
                return;
            }
            
            // Menü içeriğini al
            String menuContent = DataManager.getAllMenuItems();
            StringBuilder items = new StringBuilder();
            StringBuilder orderDetails = new StringBuilder();
            double totalAmount = 0.0;
            int itemCount = 0;
            
            // Seçilen ürünleri işle
            StringTokenizer itemIds = new StringTokenizer(input, ",");
            while (itemIds.hasMoreTokens()) {
                try {
                    int itemId = Integer.parseInt(itemIds.nextToken().trim());
                    boolean found = false;
                    
                    // Menüden ürün bilgisini bul
                    StringTokenizer lines = new StringTokenizer(menuContent, "\n");
                    while (lines.hasMoreTokens()) {
                        String line = lines.nextToken().trim();
                        if (line.isEmpty()) continue;
                        
                        StringTokenizer tokens = new StringTokenizer(line, "|");
                        if (tokens.countTokens() >= 5) {
                            String type = tokens.nextToken();
                            int id = Integer.parseInt(tokens.nextToken());
                            
                            if (id == itemId) {
                                found = true;
                                String name = tokens.nextToken();
                                double price = Double.parseDouble(tokens.nextToken());
                                String description = tokens.nextToken();
                                boolean isActive = Boolean.parseBoolean(tokens.nextToken());
                                
                                if (!isActive) {
                                    System.out.println("Ürün ID " + itemId + " aktif değil!");
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
                        System.out.println("Ürün ID " + itemId + " bulunamadı!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz ürün ID!");
                }
            }
            
            if (items.length() == 0) {
                System.out.println("Geçerli ürün bulunamadı!");
                return;
            }
            
            // Sipariş oluştur
            int orderId = orderManager.createOrder(customer.getCustomerId(), 
                                                   customer.getFullName(), 
                                                   tableNumber, 
                                                   items.toString(), 
                                                   totalAmount);
            
            // Masa durumunu güncelle (dolu yap)
            tableManager.updateTableStatus(tableNumber, true, false);
            
            // Sipariş bilgilerini yazdır
            System.out.println("\n=== SİPARİŞ BİLGİLERİ ===");
            System.out.println("Sipariş ID: " + orderId);
            System.out.println("Müşteri: " + customer.getFullName());
            System.out.println("Masa No: " + tableNumber);
            System.out.println("Ürünler:");
            System.out.print(orderDetails.toString());
            System.out.println("Toplam Tutar: " + totalAmount + " TL");
            System.out.println("Durum: Beklemede");
            System.out.println("Tarih: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            // Sipariş detaylarını txt dosyasına kaydet
            saveOrderToFile(orderId, customer.getFullName(), tableNumber, items.toString(), 
                          totalAmount, orderDetails.toString());
            
            System.out.println("\nSipariş başarıyla oluşturuldu ve kaydedildi!");
            
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir numara giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveOrderToFile(int orderId, String customerName, int tableNumber, 
                                String items, double totalAmount, String orderDetails) {
        try {
            StringBuilder orderInfo = new StringBuilder();
            orderInfo.append("=== SİPARİŞ DETAYI ===\n");
            orderInfo.append("Sipariş ID: ").append(orderId).append("\n");
            orderInfo.append("Müşteri: ").append(customerName).append("\n");
            orderInfo.append("Masa No: ").append(tableNumber).append("\n");
            orderInfo.append("Tarih: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
            orderInfo.append("Ürünler:\n");
            orderInfo.append(orderDetails);
            orderInfo.append("Toplam Tutar: ").append(totalAmount).append(" TL\n");
            orderInfo.append("Durum: Beklemede\n");
            orderInfo.append("==========================================\n\n");
            
            com.restoran.util.FileHandler.writeToFile("order_details.txt", orderInfo.toString());
        } catch (Exception e) {
            System.out.println("Sipariş dosyaya kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    private void makeReservation() {
        try {
            System.out.println("\n=== REZERVASYON ===");
            
            // Masaları göster
            System.out.println(tableManager.getAllTables());
            
            System.out.print("Telefon numaranız: ");
            String phone = scanner.nextLine().trim();
            
            System.out.print("Masa numarası: ");
            int tableNumber = Integer.parseInt(scanner.nextLine().trim());
            
            // Masa müsait mi kontrol et
            if (!tableManager.isTableAvailable(tableNumber)) {
                System.out.println("Bu masa müsait değil! Lütfen başka bir masa seçiniz.");
                return;
            }
            
            // Sadece saat al
            System.out.print("Rezervasyon saati (SS:DD formatında, örn: 19:30): ");
            String timeStr = scanner.nextLine().trim();
            
            // Bugünün tarihini al ve saat ile birleştir
            LocalDate today = LocalDate.now();
            LocalDateTime reservationDate = LocalDateTime.parse(
                today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + timeStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            );
            
            // Geçmiş saat kontrolü
            if (reservationDate.isBefore(LocalDateTime.now())) {
                System.out.println("Geçmiş bir saat seçemezsiniz!");
                return;
            }
            
            // Aynı saatte aynı masaya rezervasyon var mı kontrol et
            if (reservationManager.isReservationExists(tableNumber, reservationDate)) {
                System.out.println("Bu saatte bu masa zaten rezerve edilmiş! Lütfen başka bir saat seçiniz.");
                return;
            }
            
            int reservationId = reservationManager.createReservation(
                customer.getCustomerId(), customer.getFullName(), phone, tableNumber, reservationDate);
            
            // Masa durumunu güncelle (rezerve yap)
            tableManager.updateTableStatus(tableNumber, false, true);
            
            System.out.println("\nRezervasyon yapıldı!");
            System.out.println("Rezervasyon ID: " + reservationId);
            System.out.println("Masa: " + tableNumber);
            System.out.println("Tarih: " + reservationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Saat: " + reservationDate.format(DateTimeFormatter.ofPattern("HH:mm")));
            
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir numara giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        try {
            System.out.println("\n=== REZERVASYON İPTAL ===");
            System.out.print("İptal edilecek rezervasyon ID: ");
            int reservationId = Integer.parseInt(scanner.nextLine().trim());
            
            reservationManager.cancelReservation(reservationId, customer.getCustomerId());
            System.out.println("Rezervasyon iptal edildi!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showMyReservations() {
        try {
            String content = DataManager.getAllReservations();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nRezervasyonunuz yok!");
                return;
            }

            StringBuilder reservations = new StringBuilder();
            reservations.append("=== REZERVASYONLARIM ===\n");
            
            StringTokenizer lines = new StringTokenizer(content, "\n");
            boolean found = false;
            
            while (lines.hasMoreTokens()) {
                String line = lines.nextToken().trim();
                if (line.isEmpty()) continue;
                
                StringTokenizer tokens = new StringTokenizer(line, "|");
                if (tokens.countTokens() >= 7) {
                    int id = Integer.parseInt(tokens.nextToken());
                    int resCustomerId = Integer.parseInt(tokens.nextToken());
                    
                    // Sadece bu müşteriye ait rezervasyonları göster
                    if (resCustomerId == customer.getCustomerId()) {
                        String customerName = tokens.nextToken();
                        String customerPhone = tokens.nextToken();
                        int tableNumber = Integer.parseInt(tokens.nextToken());
                        String dateStr = tokens.nextToken();
                        int numberOfGuests = Integer.parseInt(tokens.nextToken());
                        boolean isActive = Boolean.parseBoolean(tokens.nextToken());
                        
                        if (isActive) {
                            found = true;
                            try {
                                // Nanosaniye kısmını kaldır
                                if (dateStr.contains(".")) {
                                    dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                }
                                LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                reservations.append("ID: ").append(id)
                                          .append(" | Masa: ").append(tableNumber)
                                          .append(" | Telefon: ").append(customerPhone)
                                          .append(" | Tarih: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                          .append(" | Saat: ").append(date.format(DateTimeFormatter.ofPattern("HH:mm")))
                                          .append(" | Kişi Sayısı: ").append(numberOfGuests)
                                          .append("\n");
                            } catch (Exception e) {
                                reservations.append("ID: ").append(id)
                                          .append(" | Masa: ").append(tableNumber)
                                          .append(" | Tarih: ").append(dateStr)
                                          .append("\n");
                            }
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nAktif rezervasyonunuz yok!");
            } else {
                System.out.println("\n" + reservations.toString());
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showActiveOrders() {
        try {
            String content = DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nAktif siparişiniz yok!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== AKTİF SİPARİŞLERİM ===\n");
            
            StringTokenizer lines = new StringTokenizer(content, "\n");
            boolean found = false;
            
            while (lines.hasMoreTokens()) {
                String line = lines.nextToken().trim();
                if (line.isEmpty()) continue;
                
                StringTokenizer tokens = new StringTokenizer(line, "|");
                if (tokens.countTokens() >= 7) {
                    int id = Integer.parseInt(tokens.nextToken());
                    int cId = Integer.parseInt(tokens.nextToken());
                    
                    if (cId == customer.getCustomerId()) {
                        String customerName = tokens.nextToken();
                        int tableNumber = Integer.parseInt(tokens.nextToken());
                        String items = tokens.nextToken();
                        double totalAmount = Double.parseDouble(tokens.nextToken());
                        String statusStr = tokens.nextToken();
                        
                        OrderStatus status;
                        try {
                            status = OrderStatus.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            // Status parse edilemezse atla
                            continue;
                        }
                        
                        if (status != OrderStatus.SERVED && status != OrderStatus.CANCELLED) {
                            found = true;
                            orders.append("Sipariş ID: ").append(id)
                                  .append(" | Masa: ").append(tableNumber)
                                  .append(" | Ürünler: ").append(items)
                                  .append(" | Tutar: ").append(totalAmount).append(" TL")
                                  .append(" | Durum: ").append(status.getDescription());
                            
                            // Tarih bilgisini oku
                            if (tokens.hasMoreTokens()) {
                                String dateStr = tokens.nextToken();
                                try {
                                    // Nanosaniye kısmını kaldır
                                    if (dateStr.contains(".")) {
                                        dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                    }
                                    LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    orders.append(" | Tarih: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                } catch (Exception e) {
                                    // Parse edilemezse tarih gösterme
                                }
                            }
                            orders.append("\n");
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nAktif siparişiniz yok!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showOrderHistory() {
        try {
            String content = DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nSipariş geçmişiniz yok!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== GEÇMİŞ SİPARİŞLERİM ===\n");
            
            StringTokenizer lines = new StringTokenizer(content, "\n");
            boolean found = false;
            
            while (lines.hasMoreTokens()) {
                String line = lines.nextToken().trim();
                if (line.isEmpty()) continue;
                
                StringTokenizer tokens = new StringTokenizer(line, "|");
                if (tokens.countTokens() >= 7) {
                    int id = Integer.parseInt(tokens.nextToken());
                    int cId = Integer.parseInt(tokens.nextToken());
                    
                    if (cId == customer.getCustomerId()) {
                        String customerName = tokens.nextToken();
                        int tableNumber = Integer.parseInt(tokens.nextToken());
                        String items = tokens.nextToken();
                        double totalAmount = Double.parseDouble(tokens.nextToken());
                        String statusStr = tokens.nextToken();
                        
                        OrderStatus status;
                        try {
                            status = OrderStatus.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            // Status parse edilemezse atla
                            continue;
                        }
                        
                        // Sadece SERVED veya CANCELLED olanları göster
                        if (status == OrderStatus.SERVED || status == OrderStatus.CANCELLED) {
                            found = true;
                            orders.append("Sipariş ID: ").append(id)
                                  .append(" | Masa: ").append(tableNumber)
                                  .append(" | Ürünler: ").append(items)
                                  .append(" | Tutar: ").append(totalAmount).append(" TL")
                                  .append(" | Durum: ").append(status.getDescription());
                            
                            // Tarih bilgisini oku
                            if (tokens.hasMoreTokens()) {
                                String dateStr = tokens.nextToken();
                                try {
                                    // Nanosaniye kısmını kaldır
                                    if (dateStr.contains(".")) {
                                        dateStr = dateStr.substring(0, dateStr.indexOf("."));
                                    }
                                    LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                    orders.append(" | Tarih: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                                } catch (Exception e) {
                                    // Parse edilemezse tarih gösterme
                                }
                            }
                            orders.append("\n");
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("\nSipariş geçmişiniz yok!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}
