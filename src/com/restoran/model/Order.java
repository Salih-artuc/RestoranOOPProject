package com.restoran.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sipariş sınıfı
 */
public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private int tableNumber;
    private String items; // Sipariş edilen ürünler (String olarak saklanacak)
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private int waiterId;
    private String waiterName;

    public Order(int orderId, int customerId, String customerName, int tableNumber, 
                String items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.BEKLEMEDE;
        this.orderDate = LocalDateTime.now();
        this.waiterId = 0;
        this.waiterName = "";
    }

    // Encapsulation
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return orderDate.format(formatter);
    }
}

