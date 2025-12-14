package com.restoran.service;

import com.restoran.model.Payment;
import com.restoran.exception.FileOperationException;
import com.restoran.exception.NotFoundException;

/**
 * Ödeme servisi
 */
public class PaymentService {
    private int nextPaymentId = 1;

    public Payment processPayment(int orderId, double amount, String paymentMethod) 
            throws FileOperationException {
        Payment payment = new Payment(nextPaymentId++, orderId, amount, paymentMethod);
        payment.setPaid(true);
        return payment;
    }

    public String formatPaymentInfo(Payment payment) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ödeme ID: ").append(payment.getPaymentId())
          .append(" | Sipariş ID: ").append(payment.getOrderId())
          .append(" | Tutar: ").append(payment.getAmount()).append(" TL")
          .append(" | Yöntem: ").append(payment.getPaymentMethod())
          .append(" | Tarih: ").append(payment.getFormattedDate())
          .append(" | Durum: ").append(payment.isPaid() ? "Ödendi" : "Beklemede");
        return sb.toString();
    }
}

