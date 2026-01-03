package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.InvalidInputException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Reservation;
import com.restoran.data.DataManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationManager {
    private int nextReservationId = 1;

    public ReservationManager() {
        loadNextReservationId();
    }

    private void loadNextReservationId() {
        try {
            String content = DataManager.getAllReservations();
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
                        }
                    }
                }
                nextReservationId = maxId + 1;
            }
        } catch (FileOperationException e) {
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
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 7) {
                int id = Integer.parseInt(fields[0]);
                if (id == reservationId) {
                    int resCustomerId = Integer.parseInt(fields[1]);
                    if (customerId != -1 && resCustomerId != customerId) {
                        throw new NotFoundException("This reservation doesn't belong to you!");
                    }
                    
                    String customerName = fields[2];
                    String customerPhone = fields[3];
                    tableNumber = Integer.parseInt(fields[4]);
                    String reservationDate = fields[5];
                    String numberOfGuests = fields[6];
                    StringBuilder newLine = new StringBuilder();
                    newLine.append(id).append("|")
                           .append(resCustomerId).append("|")
                           .append(customerName).append("|")
                           .append(customerPhone).append("|")
                           .append(tableNumber).append("|")
                           .append(reservationDate).append("|")
                           .append(numberOfGuests).append("|")
                           .append("false");
                    
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
        
        if (tableNumber > 0) {
            try {
                com.restoran.service.TableManager tableManager = new com.restoran.service.TableManager();
                tableManager.updateTableStatus(tableNumber, false, false);
            } catch (Exception e) {
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
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 7) {
                int id = Integer.parseInt(fields[0]);
                String customerName = fields[2];
                String customerPhone = fields[3];
                int tableNumber = Integer.parseInt(fields[4]);
                String dateStr = fields[5];
                int numberOfGuests = Integer.parseInt(fields[6]);
                boolean isActive = Boolean.parseBoolean(fields[7]);
                
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

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 7) {
                int tNumber = Integer.parseInt(fields[4]);
                String dateStr = fields[5];
                boolean isActive = Boolean.parseBoolean(fields[7]);
                
                if (isActive && tNumber == tableNumber) {
                    try {
                        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        if (date.toLocalDate().equals(reservationDate.toLocalDate()) &&
                            date.getHour() == reservationDate.getHour() &&
                            date.getMinute() == reservationDate.getMinute()) {
                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        
        return false;
    }
}

