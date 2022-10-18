package com.company.store.controller;

import com.company.store.model.LoggedInUser;
import com.company.store.model.Product;
import com.company.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private LoggedInUser loggedInUser;


    @GetMapping("products")
    public @ResponseBody List<Product> getProducts(){
        return productService.findAll();
    }

    @RequestMapping("product-catalog")
    public String getProductCatalog(Model model){
        // if user didn't log in
        if(!loggedInUser.isUserLoggedIn()){
            return "loginRequired";
        }

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "productCatalog";
    }
}