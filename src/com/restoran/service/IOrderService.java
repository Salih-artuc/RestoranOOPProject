package com.restoran.service;

/**
 * Sipariş servisi interface'i
 */
public interface IOrderService {
    int createOrder(int customerId, String customerName, int tableNumber, 
                   String items, double totalAmount) throws Exception;
    void assignWaiter(int orderId, int waiterId, String waiterName) throws Exception;
}

