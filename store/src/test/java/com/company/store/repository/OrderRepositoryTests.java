package com.company.store.repository;

import com.company.store.DatabaseConfig;
import com.company.store.model.Order;
import com.company.store.model.OrderItem;
import com.company.store.model.OrderSummary;
import com.company.store.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderRepositoryTests {


    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());

    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        System.out.println("Before setup: "  + orderRepository.findAll().size());
        databaseConfig.fillTestDatabase();
        System.out.println("After setup: "  + orderRepository.findAll().size());
    }

    @AfterEach
    public void teardown(){
        System.out.println("Before delete: "  + orderRepository.findAll().size());

        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("After delete: "  + orderRepository.findAll().size());
    }

    @Test
    public void contextLoads() {
        assertNotNull(userRepository, "UserRepository is null.");
        assertNotNull(productRepository, "ProductRepository is null.");
        assertNotNull(orderRepository, "OrderRepository is null.");
    }

    private Order getTestOrder(){
        Order order = new Order();
        order.setOrderDate(new Date().toString());
        order.setUser(userRepository.findById(1));

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(productRepository.findById(5));
        orderItem1.setQuantity(5L);

        OrderItem orderItem2 = new OrderItem();
        orderItem1.setProduct(productRepository.findById(4));
        orderItem1.setQuantity(4L);

        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        order.setOrderItems(orderItems);

        return order;
    }

    @Test
    public void whenSaveOrder_thenAddOrderToDatabase() {
        // given
        Order order = getTestOrder();
        int orderCountBefore = orderRepository.findAll().size();

        // when
        order = orderRepository.save(order);

        // then
        assertEquals(orderCountBefore + 1, orderRepository.findAll().size(), "Orders count should rise.");
        assertNotNull(order.getId(), "Order ID is null.");
        assertNotNull(order.getOrderItems().get(0).getProduct(), "OrderItem product is null.");
        assertNotNull(order.getOrderItems().get(0).getProduct(), "OrderItem product is null.");
        assertNotNull(order.getOrderDate(), "Order date is null.");
        assertEquals(2, order.getOrderItems().size(), "OrderItem array is null.");
        assertEquals(userRepository.findById(1), order.getUser(), "Incorrect user attached to the order.");
    }

    @Test
    public void whenEditOrder_thenSaveEditedOrderInDatabase(){
        // given
        Order order = getTestOrder();
        orderRepository.save(order);
        String oldDate = order.getOrderDate();
        User newOrderOwner = userRepository.findById(2);

        // when
        // init user has id = 1
        order.setUser(newOrderOwner);
        order.setOrderDate(String.valueOf(new Date().getTime()));
        orderRepository.save(order);


        // then
        assertEquals(newOrderOwner, orderRepository.findById(order.getId().intValue()).getUser(), "Order owner should've changed");
        assertNotEquals(oldDate, orderRepository.findById(order.getId().intValue()).getOrderDate(), "Order date should've changed");
    }


    @Test
    public void whenFindOrderSummaries_thenGetAllProductsSummaries(){
        // given

        // when
        List<OrderSummary> orderSummaries = orderRepository.findAllSummaries();

        // then
        assertEquals(orderRepository.findAll().size(), orderSummaries.size(), "Order count and order summaries count differ.");
        for(var orderSummary : orderSummaries){
            assertNotNull(orderSummary.getOrderId());
            assertNotNull(orderSummary.getOrderDate());
            assertNotNull(orderSummary.getSummaryPrice());
            assertNotNull(orderSummary.getUserEmail());
            assertNotNull(orderSummary.getUserFirstname());
            assertNotNull(orderSummary.getUserLastname());
        }
    }

    @Test
    public void whenFindAllOrders_thenGetAllOrdersList(){
        // given

        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(2, orders.size(), "There should be two orders in the list.");
        for(var order : orders){
            assertNotNull(order.getId());
            assertNotNull(order.getOrderDate());
            assertNotNull(order.getOrderItems());
            assertNotNull(order.getUser());
        }
    }

    @Test
    public void whenAddOrderItemsToOrder_thenGetProperSizeOfOrderEntitiesListOfProduct(){
        // given
        Order order = getTestOrder();
        int orderItemsCountBefore = order.getOrderItems().size();

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productRepository.findById(2));
        orderItem.setQuantity(2L);

        // when
        order.getOrderItems().add(orderItem);
        orderRepository.save(order);

        // then
        assertEquals(orderItemsCountBefore + 1, orderRepository.findById(order.getId().intValue()).getOrderItems().size(), "OrderItem list associated with order didn't update.");
    }
}
