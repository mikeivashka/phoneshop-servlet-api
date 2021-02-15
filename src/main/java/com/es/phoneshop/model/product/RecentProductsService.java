package com.es.phoneshop.model.product;

import java.util.Deque;

public interface RecentProductsService {
    void addToRecentProducts(Deque<Product> recent, Product productToAdd);
}
