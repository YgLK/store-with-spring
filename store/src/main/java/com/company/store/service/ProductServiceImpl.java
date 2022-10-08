package com.company.store.service;


import com.company.store.model.Product;
import com.company.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Product save(Product product){
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product setAvailableQuantity(Long productId, Long quantity) {
        return productRepository.setAvailableQuantity(productId, quantity);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int productId) {
        return productRepository.findById(productId);
    }
}
