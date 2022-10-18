package com.company.store.repository;

import com.company.store.model.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product save(Product product){
        entityManager.persist(product);

        return product;
    }

    @Override
    public Product setAvailableQuantity(Long productId, Long quantity) {
        Product toUpdate = findById(productId.intValue());
        toUpdate.setQuantityStock(quantity);

        entityManager.merge(toUpdate);

        return toUpdate;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) entityManager.createQuery("SELECT p FROM Product p").getResultList();
    }

    @Override
    public Product findById(int productId) {
        // should be done exception where product with chosen ID doesn't exist
        return (Product) entityManager.createQuery(String.format(String.format("SELECT p FROM Product p WHERE p.Id = %s", (long) productId))).getResultList().get(0);
    }

}
