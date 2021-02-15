package com.es.phoneshop.model.cart.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.impl.ArrayListProductDao;

import java.util.List;
import java.util.NoSuchElementException;

public class CartServiceImpl implements CartService {

    private final ProductDao productDao = ArrayListProductDao.getInstance();

    private CartServiceImpl() {
    }

    public static CartServiceImpl getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) {
        List<CartItem> cartItems = cart.getItems();
        if (quantity == 0) {
            delete(cart, productId);
        } else {
            Product product = productDao.getProduct(productId).orElseThrow(NoSuchElementException::new);
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
    public boolean delete(Cart cart, Long productId) {
        return cart.getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
    }

    private static class LazySingletonHolder {
        public static final CartServiceImpl HOLDER_INSTANCE = new CartServiceImpl();
    }
}
