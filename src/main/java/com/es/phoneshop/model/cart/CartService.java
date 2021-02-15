package com.es.phoneshop.model.cart;

public interface CartService {
    void update(Cart cart, Long productId, int quantity);

    boolean delete(Cart cart, Long productId);
}

