package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

public interface CartService {
    void update(Cart cart, Product product, int quantity);

    boolean delete(Cart cart, Product product);
}

