package com.company.store.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "store_order_items")
@NamedQueries({
        @NamedQuery(name=OrderItem.ORDER_ITEM_FIND_ALL, query=OrderItem.ORDER_ITEM_FIND_ALL_JPQL)
})
public class OrderItem {

    // get list of all orders
    public static final String ORDER_ITEM_FIND_ALL = "orderItemAll";
    public static final String ORDER_ITEM_FIND_ALL_JPQL = "SELECT oi FROM OrderItem oi";


    // id will be replaced with two foreign keys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long quantity;

    @JsonBackReference
    @ManyToOne
    private Order order;

    @JsonBackReference
    @ManyToOne
    private Product product;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;

    }
}
