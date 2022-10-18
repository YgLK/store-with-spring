package com.company.store.service;

import com.company.store.model.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductService {

    @Transactional
    Product save(Product product);

    @Transactional
    Product setAvailableQuantity(Long productId, Long quantity);

    List<Product> findAll();

    Product findById(int productId);
}
