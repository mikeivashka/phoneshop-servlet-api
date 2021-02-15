package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentProductsService;

import java.util.Deque;

public class RecentProductsServiceImpl implements RecentProductsService {
    private static final int RECENT_PRODUCTS_SIZE = 3;

    private RecentProductsServiceImpl() {
    }

    public static RecentProductsServiceImpl getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public void addToRecentProducts(Deque<Product> recent, Product productToAdd) {
        recent.removeFirstOccurrence(productToAdd);
        recent.add(productToAdd);
        if (recent.size() > RECENT_PRODUCTS_SIZE) {
            recent.pollFirst();
        }
    }

    private static class LazySingletonHolder {
        public static final RecentProductsServiceImpl HOLDER_INSTANCE = new RecentProductsServiceImpl();
    }
}