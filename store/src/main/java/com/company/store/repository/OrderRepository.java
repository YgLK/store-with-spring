package com.company.store.repository;

import com.company.store.model.Order;
import com.company.store.model.OrderSummary;
import com.company.store.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    List<OrderSummary> findAllSummaries();

    Order findById(int orderId);

    List<Order> findAll();

}
