package com.company.store.repository;

import com.company.store.model.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository {
    Product save(Product product);

    Product setAvailableQuantity(Long productId, Long quantity);

    List<Product> findAll();

    Product findById(int productId);
}
