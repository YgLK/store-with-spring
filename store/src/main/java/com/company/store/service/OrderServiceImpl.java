package com.company.store.service;

import com.company.store.model.Order;
import com.company.store.model.OrderSummary;
import com.company.store.model.User;
import com.company.store.repository.OrderRepository;
import com.company.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public Order save(Order order){
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderSummary> findAllSummaries() {
        return orderRepository.findAllSummaries();
    }
}
