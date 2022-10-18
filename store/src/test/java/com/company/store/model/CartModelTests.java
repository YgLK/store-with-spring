package com.company.store.model;

import com.company.store.DatabaseConfig;
import com.company.store.repository.OrderItemRepository;
import com.company.store.repository.ProductRepository;
import com.company.store.repository.UserRepositoryTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CartModelTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());
    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;


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

    private OrderItem getOrderItem(int productId, Long quantity){
        OrderItem orderItem = new OrderItem();
        Product product = productRepository.findById(productId);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        return orderItem;
    }

    @Test
    public void whenGetFilledCartTotal_thenGetProductsSummaryPrice(){
        // given
        Cart cart = new Cart();
        List<OrderItem> cartEntries = new ArrayList<>();
        // total orderItem1 price: 3 * 33 = 99
        OrderItem orderItem1 = getOrderItem(1, 3L);
        // total orderItem2 price: 5 * 11.3 = 56.5
        OrderItem orderItem2 = getOrderItem(8, 5L);
        // total orderItem3 price: 1 * 4 = 4
        OrderItem orderItem3 = getOrderItem(9, 1L);


        // add OrderItems to cart
        cartEntries.add(orderItem1);
        cartEntries.add(orderItem2);
        cartEntries.add(orderItem3);
        cart.setEntries(cartEntries);


        // when
        BigDecimal totalCartPrice = cart.getTotal();
        BigDecimal expectedCartPrice = BigDecimal.valueOf(99 + 56.5 + 4).setScale(2, RoundingMode.HALF_UP);

        // then
        assertEquals(expectedCartPrice, totalCartPrice, "Incorrect total cart price.");
    }

    @Test
    public void whenGetEmptyCartTotal_thenGetProductsSummaryPriceZero(){
        // given
        Cart cart = new Cart();

        // when
        BigDecimal totalCartPrice = cart.getTotal();
        BigDecimal expectedCartPrice = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);


        // then
        assertEquals(expectedCartPrice, totalCartPrice, "Empty cart should have price 0.");

    }

}
