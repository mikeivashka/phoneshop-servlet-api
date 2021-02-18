package com.es.phoneshop.model.cart.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
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
            updateCartStatistics(cart);
        }
    }

    @Override
    public boolean delete(Cart cart, Product product) {
        boolean deleted = cart.getItems().removeIf(cartItem -> cartItem.getProduct().equals(product));
        updateCartStatistics(cart);
        return deleted;
    }

    private void updateCartStatistics(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
        cart.setTotalPrice(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private static class LazySingletonHolder {
        public static final CartServiceImpl HOLDER_INSTANCE = new CartServiceImpl();
    }
}
