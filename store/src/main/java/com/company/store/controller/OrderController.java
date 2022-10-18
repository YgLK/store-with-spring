package com.company.store.controller;

import com.company.store.model.LoggedInUser;
import com.company.store.model.Order;
import com.company.store.model.OrderSummary;
import com.company.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private LoggedInUser loggedInUser;


    @GetMapping("order-summaries")
    public @ResponseBody List<OrderSummary> getOrderSummaries(){
        return orderService.findAllSummaries();
    }

    @GetMapping("show-orders")
    public ModelAndView showOrders(Map<String, Object> model){
        ModelAndView mv = new ModelAndView();

        // don't show the list of orders to anonymous users
        if(!loggedInUser.isUserLoggedIn()){
            mv.setStatus(HttpStatus.UNAUTHORIZED);
            mv.setViewName("loginRequired");
            return mv;
        }

        // show orders' summaries in the table
        model.put("orderSummaries", orderService.findAllSummaries());

        mv.setViewName("showOrders");
        mv.setStatus(HttpStatus.OK);
        return mv;
    }

    @GetMapping("orders")
    public @ResponseBody List<Order> getOrders(){
        return orderService.findAll();
    }


}
