package com.company.store.service;


import com.company.store.model.OrderItem;
import com.company.store.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    @Transactional
    public OrderItem save(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    };

    @Override
    @Transactional
    public List<OrderItem> saveOrderItems(List<OrderItem> orderItems){
        return orderItemRepository.saveOrderItems(orderItems);
    };

    @Override
    public List<OrderItem> findAll(){
        return orderItemRepository.findAll();
    };
}
