package com.company.store.repository;

import com.company.store.DatabaseConfig;
import com.company.store.model.Order;
import com.company.store.model.OrderItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderItemRepositoryTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());

    private final DatabaseConfig databaseConfig = new DatabaseConfig();


    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void contextLoads() {
        assertNotNull(orderItemRepository, "OrderItemRepository is null.");
        assertNotNull(productRepository, "ProductRepository is null.");
        assertNotNull(orderRepository, "OrderRepository is null.");
    }


    @BeforeEach
    public void setUp(){
        System.out.println("Before setup: "  + orderItemRepository.findAll().size());
        databaseConfig.fillTestDatabase();
        System.out.println("After setup: "  + orderItemRepository.findAll().size());
    }

    @AfterEach
    public void teardown(){
        System.out.println("Before delete: "  + orderItemRepository.findAll().size());

        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("After delete: "  + orderItemRepository.findAll().size());
    }


    private OrderItem getTestOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productRepository.findById(1));
        orderItem.setQuantity(12L);
        orderItem.setOrder(orderRepository.findById(1));

        return orderItem;
    }

    @Test
    public void whenSaveOrderItem_thenAddOrderItemToDatabase() {
        // given
        OrderItem orderItem = getTestOrderItem();
        int orderItemCountBefore = orderItemRepository.findAll().size();

        // when
        orderItemRepository.save(orderItem);

        // then
        assertEquals(orderItemCountBefore + 1, orderItemRepository.findAll().size(), "OrderItem count should rise.");
    }


    @Test
    public void whenFindAllOrderItems_thenGetAllOrderItemsList(){
        // given

        // when
        List<OrderItem> orderItems = orderItemRepository.findAll();

        // then
        assertEquals(7, orderItems.size(), "There should be seven orderItems in the list.");
        for(var orderItem : orderItems){
            assertNotNull(orderItem.getId());
            assertNotNull(orderItem.getOrder());
            assertNotNull(orderItem.getProduct());
            assertNotNull(orderItem.getQuantity());
        }
    }
}
