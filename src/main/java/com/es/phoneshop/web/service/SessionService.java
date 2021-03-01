package com.es.phoneshop.web.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;

public interface SessionService {
    <T> T castAttribute(Object attribute, Class<T> clazz);

    String updateCurrentCart(HttpSession session, Product product, int quantity);

    Cart getCart(HttpSession session);
}
