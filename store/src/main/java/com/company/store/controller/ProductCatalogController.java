package com.company.store.controller;


import com.company.store.model.LoggedInUser;
import com.company.store.model.Product;
import com.company.store.service.CartService;
import com.company.store.service.ProductService;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class ProductCatalogController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;


    @GetMapping("product-catalog")
    public ModelAndView getProductCatalog(
            Model model){
        ModelAndView mv = new ModelAndView();

        // add products to the model to show it on page
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        mv.setViewName("productCatalog");
        mv.setStatus(HttpStatus.OK);
        return mv;
    }

    @PostMapping("product-catalog")
    public ModelAndView addToCart(
            @RequestParam(value="productId", required = true) String productId,
            @RequestParam(value="quantity", required = true) String quantity,
            Model model
    ){
        Triplet<String, Model, HttpStatus> modelViewTriplet = cartService.addProductToCart(productId, quantity, model);

        model = modelViewTriplet.getValue1();

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        ModelAndView mv = new ModelAndView();
        mv.setViewName(modelViewTriplet.getValue0());
        mv.setStatus(modelViewTriplet.getValue2());

        // return view
        return mv;
    }

}
