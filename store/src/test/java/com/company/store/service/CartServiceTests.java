package com.company.store.service;

import com.company.store.DatabaseConfig;
import com.company.store.model.*;
import com.company.store.repository.UserRepositoryTests;
import org.javatuples.Triplet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CartServiceTests {

    private static final Logger LOGGER = Logger.getLogger(UserRepositoryTests.class.getName());
    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private LoggedInUser loggedInUser;
    @Autowired
    private UserService userService;
    @Autowired
    private Cart cart;
    @Mock
    private Model model;



    @BeforeEach
    public void setUp(){
        databaseConfig.fillTestDatabase();
    }

    @AfterEach
    public void teardown(){
        try {
            databaseConfig.cleanTestDatabase(databaseConfig.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void contextLoads() {
        assertNotNull(cartService, "CartService is null.");
        assertNotNull(cart, "Cart is null.");
        assertNotNull(productService, "ProductService is null.");
        assertNotNull(model, "Model is null.");
    }

    @Test
    public void whenAddToCartWithInvalidInput_thenReturnProperViewMessageHttpStatus(){
        // given
        String productId = "notProductId";
        String quantity = "stringInsteadOfNumberQuantity";

        // when
        Triplet<String, Model, HttpStatus> triplet = cartService.addProductToCart(productId, quantity, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        verify(returnedModel, times(1)).addAttribute("invalidInput", "Quantity must be a number.");
        assertEquals("productCatalog", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenAddToCartWithNegativeQuantity_thenReturnProperViewMessageHttpStatus(){
        // given
        String productId = "1";
        String quantity = "-5";

        // when
        Triplet<String, Model, HttpStatus> triplet = cartService.addProductToCart(productId, quantity, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        verify(returnedModel, times(1)).addAttribute("negativeQuantity", "Warning. Quantity value can't be negative.");
        assertEquals("productCatalog", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenAddToCartWithUnavailableQuantity_thenReturnProperViewMessageHttpStatus(){
        // given
        String productId = "1";
        String quantity = "100000000";
        Product product = productService.findById(Integer.parseInt(productId));


        // when
        Triplet<String, Model, HttpStatus> triplet = cartService.addProductToCart(productId, quantity, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        verify(returnedModel, times(1)).addAttribute(
                "incorrectQuantity",
                String.format(
                        "Warning.  Product \"%s\" doesn't have %s available pieces. The maximum is %s.",
                        product.getProductName(),
                        quantity,
                        product.getQuantityStock().toString()
                )
        );
        assertEquals("productCatalog", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenAddToCartWhenProductAlreadyInCart_thenReturnProperViewMessageHttpStatus(){
        // given
        String productId = "1";
        String quantity = "15";
        Product product = productService.findById(Integer.parseInt(productId));

        // add product with ID 1 to be in the cart before calling cartService
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(Long.valueOf("123"));
        cart.getEntries().add(orderItem);

        // when
        Triplet<String, Model, HttpStatus> triplet = cartService.addProductToCart(productId, quantity, model);

        // then
        String viewName = triplet.getValue0();
        Model returnedModel = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        verify(returnedModel, times(1)).addAttribute(
                "productAlreadyInCart",
                String.format(
                        "Product \"%s\" has already been added to the cart",
                        product.getProductName()
                )
        );
        assertEquals("productCatalog", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenAddToCartWithCorrectInput_thenReturnProperViewMessageHttpStatus(){
        // given
        String productId = "1";
        String quantity = "3";

        // when
        Triplet<String, Model, HttpStatus> triplet = cartService.addProductToCart(productId, quantity, model);

        // then
        String viewName = triplet.getValue0();
        HttpStatus httpStatus = triplet.getValue2();


        assertEquals("productCatalog", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.OK, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenUserNotLoggedInProceedOrder_thenReturnLoginRequired(){
        // given
        loggedInUser.logOut();

        // when
        Pair<String, HttpStatus> viewStatusPair = cartService.proceedOrder();

        // then
        String viewName = viewStatusPair.getFirst();
        HttpStatus httpStatus = viewStatusPair.getSecond();


        assertEquals("loginRequired", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.UNAUTHORIZED, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenCartEmptyProceedOrder_thenReturnError(){
        // given
        loggedInUser.setLoggedUser(userService.findById(1));
        cart.getEntries().clear();

        // when
        Pair<String, HttpStatus> viewStatusPair = cartService.proceedOrder();

        // then
        String viewName = viewStatusPair.getFirst();
        HttpStatus httpStatus = viewStatusPair.getSecond();


        assertEquals("error", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.BAD_REQUEST, httpStatus, "Incorrect http status returned.");
    }


    @Test
    public void whenEditProductInvalid_thenReturnProperFlashAttributeViewNameHttpStatus(){
        // given
        int productId = 3;
        long quantity = 3L;

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productService.findById(productId));
        orderItem.setQuantity(quantity);
        // add product to the cart
        cart.getEntries().add(orderItem);
        // get test redirect attributes
        final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        // when
        String newQuantity = "asdasd";
        Triplet<String, RedirectAttributes, HttpStatus> triplet = cartService.editItemInCart(String.valueOf(productId), newQuantity, redirectAttributes);


        // then
        String viewName = triplet.getValue0();
        RedirectAttributes  returnedAttributes = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        var flashAttributes = returnedAttributes.getFlashAttributes();

        assertEquals("Product ID and quantity must be numbers.", flashAttributes.get("invalidInput"), "Incorrect RedirectAttribute message has been added.");
        assertEquals("redirect:/cart", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, httpStatus, "Incorrect http status returned.");
    }

    @Test
    public void whenEditProductUnavailableQuantity_thenReturnProperFlashAttributeViewNameHttpStatus(){
        // given
        int productId = 3;
        long quantity = 3L;
        Product product = productService.findById(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        // add product to the cart
        cart.getEntries().add(orderItem);
        // get test redirect attributes
        final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        // when
        String newQuantity = "10000000";
        Triplet<String, RedirectAttributes, HttpStatus> triplet = cartService.editItemInCart(String.valueOf(productId), newQuantity, redirectAttributes);


        // then
        String viewName = triplet.getValue0();
        RedirectAttributes  returnedAttributes = triplet.getValue1();
        HttpStatus httpStatus = triplet.getValue2();

        var flashAttributes = returnedAttributes.getFlashAttributes();

        assertEquals(
                String.format(
                "Warning.  Product \"%s\" doesn't have %s available pieces. The maximum is %s.",
                product.getProductName(),
                        newQuantity,
                product.getQuantityStock().toString()),
                flashAttributes.get("incorrectQuantity"), "Incorrect RedirectAttribute message has been added.");
        assertEquals("redirect:/cart", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.CONFLICT, httpStatus, "Incorrect http status returned.");
    }


    @Test
    public void whenEditProductCorrectInput_thenReturnProperFlashAttributeViewNameHttpStatus(){
        // given
        int productId = 3;
        long quantity = 3L;
        Product product = productService.findById(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        // add product to the cart
        cart.getEntries().add(orderItem);
        // get test redirect attributes
        final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        // when
        String newQuantity = "5";
        Triplet<String, RedirectAttributes, HttpStatus> triplet = cartService.editItemInCart(String.valueOf(productId), newQuantity, redirectAttributes);


        // then
        String viewName = triplet.getValue0();
        HttpStatus httpStatus = triplet.getValue2();

        assertTrue(cart.getEntries().stream().allMatch(c -> c.getQuantity() == Long.parseLong(newQuantity)), "Product quantity isn't changed.");
        assertEquals("redirect:/cart", viewName, "Incorrect view name returned.");
        assertEquals(HttpStatus.ACCEPTED, httpStatus, "Incorrect http status returned.");
    }
}
