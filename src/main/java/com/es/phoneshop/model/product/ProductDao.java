package com.es.phoneshop.model.product;

import com.es.phoneshop.model.generic.Dao;
import com.es.phoneshop.model.product.impl.ProductSortStrategyProvider;

import java.util.List;

public interface ProductDao extends Dao<Product, Long> {
    default List<Product> findProducts() {
        return findProducts("", ProductSortStrategyProvider.empty());
    }

    default List<Product> findProducts(String query) {
        return findProducts(query, ProductSortStrategyProvider.empty());
    }

    List<Product> findProducts(String query, SortStrategyProvider<Product> sortStrategy);
}
