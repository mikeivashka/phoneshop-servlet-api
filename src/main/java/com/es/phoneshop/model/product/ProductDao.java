package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.impl.ProductSortStrategyProvider;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> getProduct(Long id);

    default List<Product> findProducts() {
        return findProducts("", ProductSortStrategyProvider.empty());
    }

    default List<Product> findProducts(String query) {
        return findProducts(query, ProductSortStrategyProvider.empty());
    }

    List<Product> findProducts(String query, SortStrategyProvider<Product> sortStrategy);

    void save(Product product);

    void delete(Long id);
}
