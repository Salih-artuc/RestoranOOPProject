package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Reservation;
import com.restoran.data.DataManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

/**
 * Rezervasyon yönetimi servisi
 */
public class ReservationManager {
    private int nextReservationId = 1;

    public ReservationManager() {
        loadNextReservationId();
    }

    private void loadNextReservationId() {
        try {
            String content = DataManager.getAllReservations();
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
                nextReservationId = maxId + 1;
            }
        } catch (FileOperationException e) {
            // İlk kullanım
        }
    }

    public int createReservation(int customerId, String customerName, String customerPhone, 
                                int tableNumber, LocalDateTime reservationDate) 
            throws InvalidInputException, FileOperationException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty!");
        }
        if (tableNumber <= 0) {
            throw new InvalidInputException("Please enter a valid table number!");
        }
        
        Reservation reservation = new Reservation(nextReservationId++, customerId, customerName, 
                                                   customerPhone, tableNumber, reservationDate, 0);
        DataManager.saveReservation(reservation);
        return nextReservationId - 1;
    }

    public void cancelReservation(int reservationId, int customerId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllReservations();
        if (content == null || content.trim().isEmpty()) {
            throw new NotFoundException("Reservation not found!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        int tableNumber = -1;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                int id = Integer.parseInt(tokens.nextToken());
                if (id == reservationId) {
                    // Müşteri ID kontrolü (customerId -1 ise kontrol yapma - işletme için)
                    int resCustomerId = Integer.parseInt(tokens.nextToken());
                    if (customerId != -1 && resCustomerId != customerId) {
                        throw new NotFoundException("This reservation doesn't belong to you!");
                    }
                    
                    // Rezervasyonu iptal et (isActive = false yap)
                    String customerName = tokens.nextToken(); // customerName
                    String customerPhone = tokens.nextToken(); // customerPhone
                    tableNumber = Integer.parseInt(tokens.nextToken()); // tableNumber
                    String reservationDate = tokens.nextToken(); // reservationDate
                    String numberOfGuests = tokens.nextToken(); // numberOfGuests
                    
                    // Yeni satır oluştur
                    StringBuilder newLine = new StringBuilder();
                    newLine.append(id).append("|")
                           .append(resCustomerId).append("|")
                           .append(customerName).append("|")
                           .append(customerPhone).append("|")
                           .append(tableNumber).append("|")
                           .append(reservationDate).append("|")
                           .append(numberOfGuests).append("|")
                           .append("false"); // isActive = false
                    
                    newContent.append(newLine.toString()).append("\n");
                    found = true;
                } else {
                    newContent.append(line).append("\n");
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Reservation not found!");
        }
        
        DataManager.updateReservationFile(newContent.toString());
        
        // Masa durumunu güncelle (rezerve durumunu kaldır)
        if (tableNumber > 0) {
            try {
                com.restoran.service.TableManager tableManager = new com.restoran.service.TableManager();
                tableManager.updateTableStatus(tableNumber, false, false);
            } catch (Exception e) {
                // Masa güncelleme hatası, devam et
            }
        }
    }

    public String getAllReservations() throws FileOperationException {
        String content = DataManager.getAllReservations();
        if (content.isEmpty()) {
            return "Rezervasyon yok!";
        }

        StringBuilder reservations = new StringBuilder();
        reservations.append("=== RESERVATIONS ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                int id = Integer.parseInt(tokens.nextToken());
                tokens.nextToken(); // customerId (atla)
                String customerName = tokens.nextToken();
                String customerPhone = tokens.nextToken();
                int tableNumber = Integer.parseInt(tokens.nextToken());
                String dateStr = tokens.nextToken();
                int numberOfGuests = Integer.parseInt(tokens.nextToken());
                boolean isActive = Boolean.parseBoolean(tokens.nextToken());
                
                if (isActive) {
                    try {
                        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        reservations.append("ID: ").append(id)
                                  .append(" | Customer: ").append(customerName)
                                  .append(" | Phone: ").append(customerPhone)
                                  .append(" | Table: ").append(tableNumber)
                                  .append(" | Date: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                                  .append(" | Number of People: ").append(numberOfGuests)
                                  .append("\n");
                    } catch (Exception e) {
                        reservations.append("ID: ").append(id)
                                  .append(" | Customer: ").append(customerName)
                                  .append(" | Table: ").append(tableNumber)
                                  .append(" | Date: ").append(dateStr)
                                  .append("\n");
                    }
                }
            }
        }
        
        return reservations.toString();
    }

    public boolean isReservationExists(int tableNumber, LocalDateTime reservationDate) 
            throws FileOperationException {
        String content = DataManager.getAllReservations();
        if (content.isEmpty()) {
            return false;
        }

        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 7) {
                tokens.nextToken(); // id
                tokens.nextToken(); // customerId
                tokens.nextToken(); // customerName
                tokens.nextToken(); // customerPhone
                int tNumber = Integer.parseInt(tokens.nextToken());
                String dateStr = tokens.nextToken();
                tokens.nextToken(); // numberOfGuests
                boolean isActive = Boolean.parseBoolean(tokens.nextToken());
                
                if (isActive && tNumber == tableNumber) {
                    try {
                        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        // Aynı gün ve aynı saat kontrolü
                        if (date.toLocalDate().equals(reservationDate.toLocalDate()) &&
                            date.getHour() == reservationDate.getHour() &&
                            date.getMinute() == reservationDate.getMinute()) {
                            return true;
                        }
                    } catch (Exception e) {
                        // Parse hatası, devam et
                    }
                }
            }
        }
        
        return false;
    }
}

