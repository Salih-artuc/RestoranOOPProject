package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Table;
import com.restoran.data.DataManager;

public class TableManager {
    public void addTable(int tableNumber, int capacity) throws FileOperationException {
        Table table = new Table(tableNumber, capacity);
        DataManager.saveTable(table);
    }

    public String getAllTables() throws FileOperationException {
        String content = DataManager.getAllTables();
        if (content.isEmpty()) {
            return "No table information!";
        }

        StringBuilder tables = new StringBuilder();
        tables.append("=== TABLES ===\n");
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 4) {
                int tableNumber = Integer.parseInt(fields[0]);
                int capacity = Integer.parseInt(fields[1]);
                boolean isOccupied = Boolean.parseBoolean(fields[2]);
                boolean isReserved = Boolean.parseBoolean(fields[3]);
                
                tables.append("Table No: ").append(tableNumber)
                      .append(" | Capacity: ").append(capacity)
                      .append(" | Status: ");
                
                if (isOccupied) {
                    tables.append("Full");
                } else if (isReserved) {
                    tables.append("Reserved");
                } else {
                    tables.append("Empty");
                }
                tables.append("\n");
            }
        }
        
        return tables.toString();
    }

    public void updateTableStatus(int tableNumber, boolean isOccupied, boolean isReserved) 
            throws NotFoundException, FileOperationException {
        String content = DataManager.getAllTables();
        if (content.isEmpty()) {
            throw new NotFoundException("Table not found!");
        }

        StringBuilder newContent = new StringBuilder();
        boolean found = false;
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 4) {
                int tNumber = Integer.parseInt(fields[0]);
                if (tNumber == tableNumber) {
                    int capacity = Integer.parseInt(fields[1]);
                    newContent.append(tNumber).append("|")
                              .append(capacity).append("|")
                              .append(isOccupied).append("|")
                              .append(isReserved).append("\n");
                    found = true;
                } else {
                    newContent.append(line).append("\n");
                }
            } else {
                newContent.append(line).append("\n");
            }
        }
        
        if (!found) {
            throw new NotFoundException("Table not found!");
        }
        
        DataManager.updateTableFile(newContent.toString());
    }

    public boolean isTableAvailable(int tableNumber) throws FileOperationException {
        String content = DataManager.getAllTables();
        if (content.isEmpty()) {
            return false;
        }

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 4) {
                int tNumber = Integer.parseInt(fields[0]);
                if (tNumber == tableNumber) {
                    boolean isOccupied = Boolean.parseBoolean(fields[2]);
                    boolean isReserved = Boolean.parseBoolean(fields[3]);
                    return !isOccupied && !isReserved;
                }
            }
        }
        return false;
    }

    public int getNextTableNumber() throws FileOperationException {
        String content = DataManager.getAllTables();
        if (content.isEmpty()) {
            return 1;
        }

        int maxTableNumber = 0;
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] fields = line.split("\\|", -1);
            if (fields.length >= 4) {
                int tableNumber = Integer.parseInt(fields[0]);
                if (tableNumber > maxTableNumber) {
                    maxTableNumber = tableNumber;
                }
            }
        }
        return maxTableNumber + 1;
    }
}

