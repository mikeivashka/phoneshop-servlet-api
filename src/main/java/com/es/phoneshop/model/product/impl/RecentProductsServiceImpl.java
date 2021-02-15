package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentProductsService;

import java.util.List;

public class RecentProductsServiceImpl implements RecentProductsService {
    private static final int RECENT_PRODUCTS_SIZE = 3;

    private RecentProductsServiceImpl() {
    }

    public static RecentProductsServiceImpl getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public void addToRecentProducts(List<Product> recent, Product productToAdd) {
        recent.remove(productToAdd);
        recent.add(productToAdd);
        if (recent.size() == RECENT_PRODUCTS_SIZE + 1) {
            recent.remove(0);
        }
    }

    private static class LazySingletonHolder {
        public static final RecentProductsServiceImpl HOLDER_INSTANCE = new RecentProductsServiceImpl();
    }
}