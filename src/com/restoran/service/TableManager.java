package com.restoran.service;

import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;
import com.restoran.model.Table;
import com.restoran.data.DataManager;
import java.util.StringTokenizer;

/**
 * Table management service
 */
public class TableManager {
    public void addTable(int tableNumber, int capacity) throws FileOperationException {
        Table table = new Table(tableNumber, capacity);
        DataManager.saveTable(table);
    }

    public String getAllTables() throws FileOperationException {
        String content = DataManager.getAllTables();
        if (content.isEmpty()) {
            return "Masa bilgisi yok!";
        }

        StringBuilder tables = new StringBuilder();
        tables.append("=== TABLES ===\n");
        
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 4) {
                int tableNumber = Integer.parseInt(tokens.nextToken());
                int capacity = Integer.parseInt(tokens.nextToken());
                boolean isOccupied = Boolean.parseBoolean(tokens.nextToken());
                boolean isReserved = Boolean.parseBoolean(tokens.nextToken());
                
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
        StringTokenizer lines = new StringTokenizer(content, "\n");
        
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 4) {
                int tNumber = Integer.parseInt(tokens.nextToken());
                if (tNumber == tableNumber) {
                    int capacity = Integer.parseInt(tokens.nextToken());
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

        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 4) {
                int tNumber = Integer.parseInt(tokens.nextToken());
                if (tNumber == tableNumber) {
                    tokens.nextToken(); // capacity
                    boolean isOccupied = Boolean.parseBoolean(tokens.nextToken());
                    boolean isReserved = Boolean.parseBoolean(tokens.nextToken());
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
        StringTokenizer lines = new StringTokenizer(content, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            if (line.isEmpty()) continue;
            
            StringTokenizer tokens = new StringTokenizer(line, "|");
            if (tokens.countTokens() >= 4) {
                int tableNumber = Integer.parseInt(tokens.nextToken());
                if (tableNumber > maxTableNumber) {
                    maxTableNumber = tableNumber;
                }
            }
        }
        return maxTableNumber + 1;
    }
}

