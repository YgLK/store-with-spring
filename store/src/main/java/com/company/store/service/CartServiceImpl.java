package com.company.store.service;

import com.company.store.model.*;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOGGER = Logger.getLogger(CartServiceImpl.class.getName() );

    @Autowired
    private Cart cart;
    @Autowired
    private ProductService productService;
    @Autowired
    private LoggedInUser loggedInUser;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;


    @Override
    public Triplet<String, Model, HttpStatus> addProductToCart(String productId, String quantity, Model model){
        // load products
        List<Product> products = productService.findAll();

        // input validation
        int productIdInt;
        long quantityLong;

        HashMap<String, Object> validationData = addProductToCartInputValidation(productId, quantity, model);

        LOGGER.info("HASHMAP SIZE: " + validationData.size());
        LOGGER.info("HASHMAP KEYS: " + validationData.keySet());


        if(validationData.containsKey("triplet")) {
            // if incorrect input
            return (Triplet<String, Model, HttpStatus>) validationData.get("triplet");
        } else {
            // if input accepted
            productIdInt = Integer.parseInt(validationData.get("productIdInt").toString());
            quantityLong = Long.parseLong(validationData.get("quantityLong").toString());
        }

        // logging
        LOGGER.info("ProductID: " + productId);
        LOGGER.info("Quantity: " + quantity);
        LOGGER.info("POST request");

        // return logic process results
        return addProductToCartLogic(products, productIdInt, quantityLong, model);
    }

    private HashMap<String, Object> addProductToCartInputValidation(String productId, String quantity, Model model){
        // input validation
        int productIdInt;
        long quantityLong;

        // store returned data in the hashmap for convenience
        HashMap<String, Object> data = new HashMap<>();

        try{
            productIdInt = Integer.parseInt(productId);
            quantityLong = Long.parseLong(quantity);
        } catch (NumberFormatException nfe){
            model.addAttribute("invalidInput", "Quantity must be a number.");
            // add data associated with incorrect input to the triplet
            Triplet<String, Model, HttpStatus> ViewModelHttpStatusTriplet = Triplet.with("productCatalog", model, HttpStatus.UNPROCESSABLE_ENTITY);
            data.put("triplet", ViewModelHttpStatusTriplet);
            return data;
        }

        LOGGER.info("QUANTITY: " + quantityLong);
        LOGGER.info("PRODUCT ID: " +productIdInt);

        data.put("productIdInt", productIdInt);
        data.put("quantityLong", quantityLong);

        return data;
    }

    private Triplet<String, Model, HttpStatus> addProductToCartLogic(List<Product> products, int productIdInt, long quantityLong, Model model){
        if(!(productIdInt >= 0 && products.stream().anyMatch(c -> c.getId() == productIdInt))){
            // add information about incorrect quantity input
            model.addAttribute("incorrectProductId", "Incorrect product ID. Try again.");
            return Triplet.with("productCatalog", model, HttpStatus.CONFLICT);
        }

        Product product = productService.findById(productIdInt);

        OrderItem orderItem = new OrderItem();
        // get product by ID and place it here
        orderItem.setProduct(product);
        orderItem.setQuantity(quantityLong);

        // if user entered negative quantity value
        if(quantityLong < 0){
            // add information about incorrect quantity input
            model.addAttribute("negativeQuantity", "Warning. Quantity value can't be negative.");
            return Triplet.with("productCatalog", model, HttpStatus.CONFLICT);
        }
        // if user want to add higher quantity than available return prompt
        else if(product.getQuantityStock() < quantityLong){
            // add information about incorrect quantity input
            model.addAttribute(
                    "incorrectQuantity",
                    String.format(
                            "Warning.  Product \"%s\" doesn't have %s available pieces. The maximum is %s.",
                            product.getProductName(),
                            quantityLong,
                            product.getQuantityStock().toString()
                    )
            );
            return Triplet.with("productCatalog", model, HttpStatus.CONFLICT);
        }
        // if cart includes entry associated with chosen product return prompt
        else if(cart.getEntries().stream()
                .anyMatch(
                        c -> Objects.equals(c.getProduct().getId(), product.getId()))
        ){
            model.addAttribute(
                    "productAlreadyInCart",
                    String.format(
                            "Product \"%s\" has already been added to the cart",
                            product.getProductName()
                    )
            );
            return Triplet.with("productCatalog", model, HttpStatus.CONFLICT);
        }
        // save orderItem to cart
        else {
            cart.getEntries().add(orderItem);
        }
        return Triplet.with("productCatalog", model, HttpStatus.OK);
    }


    @Override
    @Transactional
    public Pair<String, HttpStatus> proceedOrder(){
        // if no user is logged in
        if(!loggedInUser.isUserLoggedIn()){
            return Pair.of("loginRequired", HttpStatus.UNAUTHORIZED);
        }

        // if cart is empty
        if(cart.getEntries().size() == 0){
            return Pair.of("error", HttpStatus.BAD_REQUEST);
        }

        // set order owner as logged-in user
        Order order = new Order();
        order.setOrderDate(new Date().toString());
        order.setUser(loggedInUser.getLoggedUser());

        // save order
        order = orderService.save(order);

        // set created order for each entry
        for(var entry : cart.getEntries()){
            entry.setOrder(order);
        }
        // save entries in the db
        orderItemService.saveOrderItems(cart.getEntries());

        // set orderItems associated with the order and save it
        order.setOrderItems(cart.getEntries());
        // update the order
        order = orderService.save(order);

        // update available quantity in the Product table in db
        cart.getEntries().stream().forEach(
                c -> productService.setAvailableQuantity(
                        c.getProduct().getId(),
                        c.getProduct().getQuantityStock() - c.getQuantity()
                )
        );

        // clear orderItem list after successfully creating order
        cart.getEntries().clear();

        return Pair.of("orderPlaced", HttpStatus.ACCEPTED);
    }


    @Override
    public Triplet<String, RedirectAttributes, HttpStatus> editItemInCart(
            String productId,
            String newQuantity,
            final RedirectAttributes redirectAttributes)
    {
        List<OrderItem> cartEntries = cart.getEntries();
        // cast productId and quantity strings to long
        Long productIdLong = Long.parseLong(productId);
        Long quantity;

        Product product = productService.findById(productIdLong.intValue());

        // input validation
        try{
            quantity = Long.parseLong(newQuantity);
        } catch(NumberFormatException nfe){
            redirectAttributes.addFlashAttribute("invalidInput", "Product ID and quantity must be numbers.");
            return Triplet.with("redirect:/cart", redirectAttributes, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // if user entered negative quantity value
        if(quantity < 0){
            // add information about incorrect quantity input
                // redirect attributes are used because view is redirected to the cart
                // and simple model's attribute doesn't save the message to prompt
            redirectAttributes.addFlashAttribute("negativeQuantity", "Warning. Quantity value can't be negative.");
            return Triplet.with("redirect:/cart", redirectAttributes, HttpStatus.CONFLICT);
        }

        // if user want to add higher quantity than available return prompt
        if(productService.findById(productIdLong.intValue()).getQuantityStock() < quantity){
                // redirect attributes are used because view is redirected to the cart
                // and simple model's attribute didn't save the message to prompt
            redirectAttributes.addFlashAttribute("incorrectQuantity",
                    String.format(
                        "Warning.  Product \"%s\" doesn't have %s available pieces. The maximum is %s.",
                        product.getProductName(),
                        quantity,
                        product.getQuantityStock().toString()
                    )
            );
            return Triplet.with("redirect:/cart", redirectAttributes, HttpStatus.CONFLICT);
        }

        // set new quantity to the corresponding cart entry
        cartEntries.stream()
                .filter(c -> Objects.equals(c.getProduct().getId(), productIdLong))
                .collect(Collectors.toList())
                .get(0)
                .setQuantity(quantity);

        return Triplet.with("redirect:/cart", redirectAttributes, HttpStatus.ACCEPTED);
    }
}
