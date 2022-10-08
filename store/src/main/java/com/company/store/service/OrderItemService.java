package com.company.store.service;

import com.company.store.model.OrderItem;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderItemService {
    @Transactional
    OrderItem save(OrderItem orderItem);

    List<OrderItem> saveOrderItems(List<OrderItem> orderItems);

    List<OrderItem> findAll();
}
