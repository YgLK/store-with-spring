package com.company.store.service;

import com.company.store.model.Order;
import com.company.store.model.OrderSummary;

import java.util.List;

public interface OrderService {
    Order save(Order order);

    List<OrderSummary> findAllSummaries();

    List<Order> findAll();
}
