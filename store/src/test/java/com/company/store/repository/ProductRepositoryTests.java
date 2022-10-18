package com.company.store.repository;

import com.company.store.DatabaseConfig;
import com.company.store.model.Product;
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
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ProductRepositoryTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());

    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp(){
        System.out.println("Before setup: "  + productRepository.findAll().size());
        databaseConfig.fillTestDatabase();
        System.out.println("After setup: "  + productRepository.findAll().size());
    }

    @AfterEach
    public void teardown(){
        System.out.println("Before delete: "  + productRepository.findAll().size());
        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("After delete: "  + productRepository.findAll().size());
    }


    @Test
    public void contextLoads() {
        assertNotNull(productRepository, "ProductRepository is null.");
    }

    @Test
    public void whenSaveProduct_thenAddProductToDB() {
        // given
        Product product = new Product();
        product.setProductName("testProduct");
        product.setQuantityStock(123L);
        product.setPrice(BigDecimal.valueOf(123.45));
        int productsCountBefore = productRepository.findAll().size();

        // when
        product = productRepository.save(product);

        // then
        assertEquals(productsCountBefore + 1, productRepository.findAll().size(), "Products count should rise.");
        assertEquals("testProduct", product.getProductName(), "Product's name doesn't match.");
        assertEquals(123L, product.getQuantityStock(), "Product's quantity stock name doesn't match.");
        assertEquals(BigDecimal.valueOf(123.45), product.getPrice(), "Product's price doesn't match.");
    }

    @Test
    public void whenFindProductByCorrectId_thenGetProductWithProperId(){
        // given
        int id = 7;

        // when
        Product product = productRepository.findById(id);

        // then
        assertEquals(BigDecimal.valueOf(2.50).setScale(2, RoundingMode.CEILING), product.getPrice(), "Product's price doesn't match.");
        assertEquals("Pepper chips", product.getProductName(), "Product's name doesn't match.");
        assertEquals(120, product.getQuantityStock(), "Product's quantity stock doesn't match.");
    }


    @Test
    public void whenFindAllProducts_thenGetAllProductsList(){
        // given

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertEquals(9, products.size(), "There should be nine products in the list.");
        for(var product : products){
            assertNotNull(product.getId(), "Product ID is null.");
            assertNotNull(product.getProductName(), "Product name is null.");
            assertNotNull(product.getQuantityStock(), "Product quantity in stock is null.");
            assertNotNull(product.getPrice(), "Product price is null.");
        }
    }

}
