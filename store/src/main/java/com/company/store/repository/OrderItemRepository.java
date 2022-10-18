package com.company.store.repository;

import com.company.store.model.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    OrderItem save(OrderItem order);

    List<OrderItem> saveOrderItems(List<OrderItem> orderItems);

    OrderItem findById(int orderItemId);

    List<OrderItem> findAll();
}
