package com.company.store.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_orders")
@NamedQueries({
        @NamedQuery(name=Order.ORDER_FIND_ALL, query=Order.ORDER_FIND_ALL_JPQL),
        @NamedQuery(name=Order.ORDER_SUMMARIES, query=Order.ORDER_SUMMARIES_JPQL)
})
public class Order {

    // get list of all orders
    public static final String ORDER_FIND_ALL = "orderAll";
    public static final String ORDER_FIND_ALL_JPQL = "SELECT o FROM Order o";


    // get list of all orders summaries
    public static final String ORDER_SUMMARIES = "orderSummaries";
    public static final String ORDER_SUMMARIES_JPQL = "SELECT new com.company.store.model.OrderSummary" +
            "(so.Id, so.orderDate, su.firstname, su.lastname, su.email, SUM(soi.quantity * sp.price))" +
            "FROM User su INNER JOIN su.orders so INNER JOIN so.orderItems soi INNER JOIN soi.product sp GROUP BY so.Id";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String orderDate;

    @JsonBackReference
    @ManyToOne
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
