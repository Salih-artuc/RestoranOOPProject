package com.restoran.model;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private int tableNumber;
    private String items;
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
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        this.waiterId = 0;
        this.waiterName = "";
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }
}

