package com.company.store.model;

import java.math.BigDecimal;

public class OrderSummary {

    private Long orderId;
    private String orderDate;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private BigDecimal summaryPrice;


    public OrderSummary(Long orderId, String orderDate, String userFirstname, String userLastname, String userEmail, BigDecimal summaryPrice) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userEmail = userEmail;
        this.summaryPrice = summaryPrice;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getSummaryPrice() {
        return summaryPrice;
    }

    public void setSummaryPrice(BigDecimal summaryPrice) {
        this.summaryPrice = summaryPrice;
    }
}
