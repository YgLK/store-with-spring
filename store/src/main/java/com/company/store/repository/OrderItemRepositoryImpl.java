package com.company.store.repository;

import com.company.store.model.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private static final java.util.logging.Logger LOGGER = Logger.getLogger(OrderItemRepositoryImpl.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderItem save(OrderItem orderItem){

        entityManager.persist(orderItem);

        return orderItem;
    }

    @Override
    public List<OrderItem> saveOrderItems(List<OrderItem> orderItems) {

        orderItems.forEach(entityManager::persist);

        return orderItems;
    }

    @Override
    public OrderItem findById(int orderItemId) {
        try {
            return (OrderItem) entityManager.createQuery(String.format("SELECT o FROM OrderItem o WHERE o.Id = %s", (long) orderItemId)).getSingleResult();
        } catch (NoResultException noe){
            // if no OrderItem associated with entered ID found
            LOGGER.warning("No OrderItem found.\n" + noe);
            // return null which is handled close to the usage
            return null;
        } catch (NonUniqueResultException nue){
            // if no unique OrderItem found
            LOGGER.warning("No unique OrderItem found.\n" + nue);
            // return null which is handled closer to the usage
            return null;
        }
    }

    @Override
    public List<OrderItem> findAll() {
        return (List<OrderItem>) entityManager.createNamedQuery(OrderItem.ORDER_ITEM_FIND_ALL).getResultList();
    }
}
