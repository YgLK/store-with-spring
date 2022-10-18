package com.company.store.model;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class Cart {

    private List<OrderItem> entries = new ArrayList<>();

    public List<OrderItem> getEntries() {
        return entries;
    }

    public void setEntries(List<OrderItem> entries) {
        this.entries = entries;
    }

    public BigDecimal getTotal() {
        if(entries.isEmpty()){
            return BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(
                    entries.stream()
                        .mapToDouble(
                                c -> c.getProduct().getPrice().doubleValue() * c.getQuantity().doubleValue()
                        )
                        .sum()
                )
                .setScale(2, RoundingMode.HALF_UP);
    }
}
