package com.company.store.repository;

import com.company.store.model.Order;
import com.company.store.model.OrderSummary;
import com.company.store.model.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order save(Order order){
        if(order.getId() == null){
            entityManager.persist(order);
        } else {
            entityManager.merge(order);
        }

        return order;
    }

    @Override
    public List<OrderSummary> findAllSummaries() {
        // JPQL query to retrieve summary for each order
        return (List<OrderSummary>) entityManager.createNamedQuery(Order.ORDER_SUMMARIES).getResultList();
    }

    @Override
    public Order findById(int orderId) {
        // should be done exception where product with chosen ID doesn't exist
        return (Order) entityManager.createQuery(String.format(String.format("SELECT o FROM Order o WHERE o.Id = %s", (long) orderId))).getResultList().get(0);
    }

    @Override
    public List<Order> findAll() {
        return (List<Order>) entityManager.createNamedQuery(Order.ORDER_FIND_ALL).getResultList();
    }
}
