package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Waiter;
import com.restoran.data.DataManager;

public class WaiterManager {
    private int nextWaiterId = 1;

    public WaiterManager() {
        loadNextWaiterId();
    }

    private void loadNextWaiterId() {
        try {
            String content = DataManager.getAllWaiters();
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
                nextWaiterId = maxId + 1;
            }
        } catch (FileOperationException e) {
        }
    }

    public void addWaiter(String name, String surname, String phoneNumber) throws FileOperationException {
        Waiter waiter = new Waiter(nextWaiterId++, name, surname, phoneNumber);
        DataManager.saveWaiter(waiter);
    }

    public String getAllWaiters() throws FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            return "No waiter information!";
        }

        StringBuilder waiters = new StringBuilder();
        waiters.append("=== WAITERS ===\n");
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                int waiterId = Integer.parseInt(fields[0]);
                String name = fields[1];
                String surname = fields[2];
                String phoneNumber = fields[3];
                boolean isAvailable = Boolean.parseBoolean(fields[4]);
                
                waiters.append("ID: ").append(waiterId)
                       .append(" | Name Surname: ").append(name).append(" ").append(surname)
                       .append(" | Phone: ").append(phoneNumber)
                       .append(" | Status: ").append(isAvailable ? "Available" : "Busy")
                       .append("\n");
            }
        }
        
        return waiters.toString();
    }

    public String getWaiterInfo(int waiterId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            throw new NotFoundException("Waiter not found!");
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                int wId = Integer.parseInt(fields[0]);
                if (wId == waiterId) {
                    String name = fields[1];
                    String surname = fields[2];
                    String phoneNumber = fields[3];
                    boolean isAvailable = Boolean.parseBoolean(fields[4]);
                    
                    return name + " " + surname;
                }
            }
        }
        
        throw new NotFoundException("Waiter not found!");
    }

    public void removeWaiter(int waiterId) throws NotFoundException, FileOperationException {
        String content = DataManager.getAllWaiters();
        if (content.isEmpty()) {
            throw new NotFoundException("Waiter not found!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 5) {
                int wId = Integer.parseInt(fields[0]);
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
            throw new NotFoundException("Waiter not found!");
        }
        
        DataManager.updateWaiterFile(newContent.toString());
    }
}
