package com.es.phoneshop.model.cart.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.product.Product;

import java.util.Deque;

public class CartServiceImpl implements CartService {

    private CartServiceImpl() {
    }

    public static CartServiceImpl getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public void update(Cart cart, Product product, int quantity) {
        Deque<CartItem> cartItems = cart.getItems();
        if (quantity == 0) {
            delete(cart, product);
        } else {
            if (quantity > product.getStock()) {
                throw new OutOfStockException(product.getDescription(), quantity, product.getStock());
            }
            cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny()
                    .ifPresentOrElse(
                            cartItem -> cartItem.setQuantity(quantity),
                            () -> cartItems.add(new CartItem(product, quantity))
                    );
        }
    }

    @Override
    public boolean delete(Cart cart, Product product) {
        return cart.getItems().removeIf(cartItem -> cartItem.getProduct().equals(product));
    }

    private static class LazySingletonHolder {
        public static final CartServiceImpl HOLDER_INSTANCE = new CartServiceImpl();
    }
}
