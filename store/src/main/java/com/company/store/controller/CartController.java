package com.company.store.controller;


import com.company.store.model.Cart;
import com.company.store.model.LoggedInUser;
import com.company.store.model.OrderItem;
import com.company.store.model.Product;
import com.company.store.service.CartService;
import com.company.store.service.ProductService;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private Cart cart;
    @Autowired
    private LoggedInUser loggedInUser;


    @GetMapping("cart")
    public String addToCart(Map<String, Object> model){
        model.put("orderEntries", cart.getEntries());
        model.put("total", cart.getTotal());

        return "cartBootstrap";
    }


    @PostMapping("add-to-cart")
    public @ResponseBody void addToCart(
            @RequestParam(value="productId", required = true) String productId,
            @RequestParam(value="quantity", required = true) String quantity
    ){
        int productIdInt = Integer.parseInt(productId);
        OrderItem orderItem = new OrderItem();
        // get product by ID
        Product product = productService.findById(productIdInt);
        orderItem.setProduct(product);
        Long quantityLong = Long.valueOf(quantity);
        orderItem.setQuantity(quantityLong);

        // save orderItem to cart
        cart.getEntries().add(orderItem);
    }

    @PostMapping("proceed-order")
    public ModelAndView proceedOrder(){
        ModelAndView mv = new ModelAndView();

        // don't proceedOrder when User not logged in
        if(!loggedInUser.isUserLoggedIn()){
            mv.setStatus(HttpStatus.UNAUTHORIZED);
            mv.setViewName("loginRequired");
            return mv;
        }

        Pair<String, HttpStatus> viewStatusPair = cartService.proceedOrder();
        mv.setViewName(viewStatusPair.getFirst());
        mv.setStatus(viewStatusPair.getSecond());

        // return view
        return mv;
    }

    @PostMapping("cart/remove-item/{productId}")
    public String removeItemFromCart(@PathVariable String productId){
        List<OrderItem> cartEntries = cart.getEntries();
        // cast productId string to long
        Long productIdLong = Long.parseLong(productId);
        // remove entry with the chosen productId from cart
        cartEntries = cartEntries.stream()
                .filter(c -> !Objects.equals(c.getProduct().getId(), productIdLong))
                .collect(Collectors.toList());

        // reassign edited cartEntries to the cart
        cart.setEntries(cartEntries);

        return "redirect:/cart";
    }


    @PostMapping("cart/edit")
    public ModelAndView editItemInCart(@RequestParam String productId,
                                           @RequestParam String newQuantity,
                                           Model model, final RedirectAttributes redirectAttributes){
        Triplet<String, RedirectAttributes, HttpStatus> modelViewTriplet = cartService.editItemInCart(productId, newQuantity, redirectAttributes);

        // return view
        return new ModelAndView(modelViewTriplet.getValue0());
    }
}
