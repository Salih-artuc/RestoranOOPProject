package com.restoran.ui;

import com.restoran.model.*;
import com.restoran.service.*;
import java.util.Scanner;

/**
 * İşletme arayüzü
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
        System.out.println("\n=== İŞLETME PANELİ ===");
        System.out.println("Hoş geldiniz " + business.getBusinessName() + "!");
        System.out.println("Yönetici: " + business.getFullName());
        
        showMenu();
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n=== İŞLETME MENÜSÜ ===");
            System.out.println("1. Menü");
            System.out.println("2. Ürün Ekleme");
            System.out.println("3. Ürün Silme");
            System.out.println("4. Fiyat Değiştirme");
            System.out.println("5. Aktif Siparişler");
            System.out.println("6. Geçmiş Siparişler");
            System.out.println("7. Sipariş Durumu Güncelleme");
            System.out.println("8. Masa Bilgileri");
            System.out.println("9. Rezervasyonlar");
            System.out.println("10. Rezervasyon İptal");
            System.out.println("11. Garson Atama");
            System.out.println("12. Garson Ekleme");
            System.out.println("13. Garson Çıkarma");
            System.out.println("14. Masa Ekleme");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            
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

    private void showMenuManagement() {
        try {
            System.out.println("\n" + menuManager.displayMenu());
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void addProduct() {
        try {
            System.out.println("\n=== ÜRÜN EKLEME ===");
            System.out.println("1. Yemek");
            System.out.println("2. Tatlı");
            System.out.print("Seçiminiz: ");
            String type = scanner.nextLine().trim();
            
            System.out.print("Ürün adı: ");
            String name = scanner.nextLine().trim();
            System.out.print("Fiyat: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Açıklama: ");
            String description = scanner.nextLine().trim();
            
            if (type.equals("1")) {
                System.out.print("Kategori (Ana yemek, Çorba, Salata vb.): ");
                String category = scanner.nextLine().trim();
                menuManager.addFood(name, price, description, category);
                System.out.println("Yemek eklendi!");
            } else if (type.equals("2")) {
                System.out.print("Tatlı tipi (Sütlü, Şerbetli, Dondurma vb.): ");
                String dessertType = scanner.nextLine().trim();
                menuManager.addDessert(name, price, description, dessertType);
                System.out.println("Tatlı eklendi!");
            } else {
                System.out.println("Geçersiz seçim!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir fiyat giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            System.out.println("\n=== ÜRÜN SİLME ===");
            System.out.println(menuManager.displayMenu());
            System.out.print("Silinecek ürün ID'si: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            menuManager.deleteItem(itemId);
            System.out.println("Ürün silindi!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void updatePrice() {
        try {
            System.out.println("\n=== FİYAT DEĞİŞTİRME ===");
            System.out.println(menuManager.displayMenu());
            System.out.print("Fiyatı değiştirilecek ürün ID'si: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Yeni fiyat: ");
            double newPrice = Double.parseDouble(scanner.nextLine().trim());
            menuManager.updatePrice(itemId, newPrice);
            System.out.println("Fiyat güncellendi!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir numara giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showActiveOrders() {
        try {
            String content = com.restoran.data.DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nAktif sipariş yok!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== AKTİF SİPARİŞLER ===\n");
            
            java.util.StringTokenizer lines = new java.util.StringTokenizer(content, "\n");
            boolean hasActive = false;
            
            while (lines.hasMoreTokens()) {
                String line = lines.nextToken().trim();
                if (line.isEmpty()) continue;
                
                java.util.StringTokenizer tokens = new java.util.StringTokenizer(line, "|");
                if (tokens.countTokens() >= 7) {
                    int id = Integer.parseInt(tokens.nextToken());
                    int customerId = Integer.parseInt(tokens.nextToken());
                    String customerName = tokens.nextToken();
                    int tableNumber = Integer.parseInt(tokens.nextToken());
                    String items = tokens.nextToken();
                    double totalAmount = Double.parseDouble(tokens.nextToken());
                    String statusStr = tokens.nextToken();
                    
                    com.restoran.model.OrderStatus status;
                    try {
                        status = com.restoran.model.OrderStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        // Status parse edilemezse atla
                        continue;
                    }
                    
                    if (status != com.restoran.model.OrderStatus.SERVED && status != com.restoran.model.OrderStatus.CANCELLED) {
                        hasActive = true;
                        orders.append("Sipariş ID: ").append(id)
                              .append(" | Müşteri: ").append(customerName)
                              .append(" | Masa: ").append(tableNumber)
                              .append(" | Ürünler: ").append(items)
                              .append(" | Tutar: ").append(totalAmount).append(" TL")
                              .append(" | Durum: ").append(status.getDescription());
                        
                        // Tarih bilgisini oku
                        if (tokens.hasMoreTokens()) {
                            String dateStr = tokens.nextToken(); // orderDate
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
                            
                            // Garson bilgisini oku
                            if (tokens.hasMoreTokens()) {
                                try {
                                    int waiterId = Integer.parseInt(tokens.nextToken());
                                    if (waiterId > 0 && tokens.hasMoreTokens()) {
                                        orders.append(" | Garson: ").append(tokens.nextToken());
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
                System.out.println("\nAktif sipariş yok!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showOrderHistory() {
        try {
            String content = com.restoran.data.DataManager.getAllOrders();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("\nGeçmiş sipariş yok!");
                return;
            }

            StringBuilder orders = new StringBuilder();
            orders.append("=== GEÇMİŞ SİPARİŞLER ===\n");
            
            java.util.StringTokenizer lines = new java.util.StringTokenizer(content, "\n");
            boolean found = false;
            
            while (lines.hasMoreTokens()) {
                String line = lines.nextToken().trim();
                if (line.isEmpty()) continue;
                
                java.util.StringTokenizer tokens = new java.util.StringTokenizer(line, "|");
                if (tokens.countTokens() >= 7) {
                    int id = Integer.parseInt(tokens.nextToken());
                    int customerId = Integer.parseInt(tokens.nextToken());
                    String customerName = tokens.nextToken();
                    int tableNumber = Integer.parseInt(tokens.nextToken());
                    String items = tokens.nextToken();
                    double totalAmount = Double.parseDouble(tokens.nextToken());
                    String statusStr = tokens.nextToken();
                    
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
                        orders.append("Sipariş ID: ").append(id)
                              .append(" | Müşteri: ").append(customerName)
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
                System.out.println("\nGeçmiş sipariş yok!");
            } else {
                System.out.println("\n" + orders.toString());
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void showTableInfo() {
        try {
            System.out.println("\n" + tableManager.getAllTables());
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        try {
            System.out.println("\n=== SİPARİŞ DURUMU GÜNCELLEME ===");
            showActiveOrders();
            System.out.print("Durumu güncellenecek sipariş ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println("\nSipariş Durumları:");
            System.out.println("1. Beklemede");
            System.out.println("2. Hazırlanıyor");
            System.out.println("3. Hazır");
            System.out.println("4. Servis Edildi");
            System.out.println("5. İptal");
            System.out.print("Yeni durum seçiniz (1-5): ");
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
                    System.out.println("Geçersiz seçim!");
                    return;
            }
            
            // Siparişin masa numarasını al
            int tableNumber = orderManager.getOrderTableNumber(orderId);
            
            // Sipariş durumunu güncelle
            orderManager.updateOrderStatus(orderId, newStatus);
            
            // Eğer sipariş SERVED veya CANCELLED ise masayı boşalt
            if (newStatus == com.restoran.model.OrderStatus.SERVED || 
                newStatus == com.restoran.model.OrderStatus.CANCELLED) {
                if (tableNumber > 0) {
                    tableManager.updateTableStatus(tableNumber, false, false);
                    System.out.println("Masa " + tableNumber + " boşaltıldı!");
                }
            }
            
            System.out.println("Sipariş durumu güncellendi: " + newStatus.getDescription());
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
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
            System.out.println("\n=== GARSON ATAMA ===");
            showActiveOrders();
            System.out.print("Sipariş ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println(waiterManager.getAllWaiters());
            System.out.print("Garson ID: ");
            int waiterId = Integer.parseInt(scanner.nextLine().trim());
            
            // Garson ID kontrolü
            String waiterName = waiterManager.getWaiterInfo(waiterId);
            orderManager.assignWaiter(orderId, waiterId, waiterName);
            System.out.println("Garson atandı!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (com.restoran.exception.NotFoundException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void addWaiter() {
        try {
            System.out.println("\n=== GARSON EKLEME ===");
            System.out.print("Ad: ");
            String name = scanner.nextLine().trim();
            System.out.print("Soyad: ");
            String surname = scanner.nextLine().trim();
            System.out.print("Telefon: ");
            String phone = scanner.nextLine().trim();
            
            waiterManager.addWaiter(name, surname, phone);
            System.out.println("Garson eklendi!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        try {
            System.out.println("\n=== REZERVASYON İPTAL ===");
            System.out.println(reservationManager.getAllReservations());
            System.out.print("İptal edilecek rezervasyon ID: ");
            int reservationId = Integer.parseInt(scanner.nextLine().trim());
            
            reservationManager.cancelReservation(reservationId, -1); // -1 = işletme, kontrol yapma
            System.out.println("Rezervasyon iptal edildi!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void removeWaiter() {
        try {
            System.out.println("\n=== GARSON ÇIKARMA ===");
            System.out.println(waiterManager.getAllWaiters());
            System.out.print("Çıkarılacak garson ID: ");
            int waiterId = Integer.parseInt(scanner.nextLine().trim());
            
            waiterManager.removeWaiter(waiterId);
            System.out.println("Garson çıkarıldı!");
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir ID giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void addTable() {
        try {
            System.out.println("\n=== MASA EKLEME ===");
            System.out.print("Kapasite: ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            
            // Otomatik masa numarası atama
            int nextTableNumber = tableManager.getNextTableNumber();
            tableManager.addTable(nextTableNumber, capacity);
            System.out.println("Masa eklendi! Masa numarası: " + nextTableNumber);
        } catch (NumberFormatException e) {
            System.out.println("Geçerli bir numara giriniz!");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}

