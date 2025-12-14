package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Waiter;
import com.restoran.data.DataManager;
import java.util.StringTokenizer;

/**
 * Garson yönetimi servisi
 */
public class WaiterManager {
    private int nextWaiterId = 1;

    public WaiterManager() {
        loadNextWaiterId();
    }

    private void loadNextWaiterId() {
        try {
            String content = DataManager.getAllWaiters();
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
                nextWaiterId = maxId + 1;
            }
        } catch (FileOperationException e) {
            // İlk kullanım
        }
    }

    public void addWaiter(String name, String surname, String phoneNumber) throws FileOperationException {
        Waiter waiter = new Waiter(nextWaiterId++, name, surname, phoneNumber);
        DataManager.saveWaiter(waiter);
    }

    public String getAllWaiters() throws FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            return "Garson bilgisi yok!";
        }

        StringBuilder waiters = new StringBuilder();
        waiters.append("=== GARSONLAR ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 5) {
                int waiterId = Integer.parseInt(tokens.nextToken());
                String name = tokens.nextToken();
                String surname = tokens.nextToken();
                String phoneNumber = tokens.nextToken();
                boolean isAvailable = Boolean.parseBoolean(tokens.nextToken());
                
                waiters.append("ID: ").append(waiterId)
                       .append(" | Ad Soyad: ").append(name).append(" ").append(surname)
                       .append(" | Telefon: ").append(phoneNumber)
                       .append(" | Durum: ").append(isAvailable ? "Müsait" : "Meşgul")
                       .append("\n");
            }
        }
        
        return waiters.toString();
    }

    public String getWaiterInfo(int waiterId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            throw new NotFoundException("Garson bulunamadı!");
        }

        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 5) {
                int wId = Integer.parseInt(tokens.nextToken());
                if (wId == waiterId) {
                    String name = tokens.nextToken();
                    String surname = tokens.nextToken();
                    String phoneNumber = tokens.nextToken();
                    boolean isAvailable = Boolean.parseBoolean(tokens.nextToken());
                    
                    return name + " " + surname;
                }
            }
        }
        
        throw new NotFoundException("Garson bulunamadı!");
    }

    public void removeWaiter(int waiterId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            throw new NotFoundException("Garson bulunamadı!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 5) {
                int wId = Integer.parseInt(tokens.nextToken());
                if (wId != waiterId) {
                    newContent.append(line).append("\n");
                } else {
                    found = true;
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Garson bulunamadı!");
        }
        
        DataManager.updateWaiterFile(newContent.toString());
    }
}

