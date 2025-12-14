package com.restoran.service;

import com.restoran.exception.FileOperationException;
import java.util.StringTokenizer;

/**
 * Rapor servisi
 */
public class ReportService {
    private OrderManager orderManager;

    public ReportService(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public String generateDailyReport() throws FileOperationException {
        StringBuilder report = new StringBuilder();
        report.append("=== GÜNLÜ RAPOR ===\n");
        report.append(orderManager.getOrderHistory());
        return report.toString();
    }

    public double calculateTotalRevenue() throws FileOperationException {
        String orders = orderManager.getOrderHistory();
        double total = 0.0;
        
        StringTokenizer lines = new StringTokenizer(orders, "\n");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken();
            if (line.contains("Tutar:")) {
                int tutarIndex = line.indexOf("Tutar: ");
                if (tutarIndex != -1) {
                    int tlIndex = line.indexOf(" TL", tutarIndex);
                    if (tlIndex != -1) {
                        try {
                            String amountStr = line.substring(tutarIndex + 7, tlIndex).trim();
                            total += Double.parseDouble(amountStr);
                        } catch (NumberFormatException e) {
                            // Ignore
                        }
                    }
                }
            }
        }
        
        return total;
    }
}

