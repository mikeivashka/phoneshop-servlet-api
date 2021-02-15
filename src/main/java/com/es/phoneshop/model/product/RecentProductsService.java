package com.es.phoneshop.model.product;

import java.util.List;

public interface RecentProductsService {
    void addToRecentProducts(List<Product> recent, Product productToAdd);
}
