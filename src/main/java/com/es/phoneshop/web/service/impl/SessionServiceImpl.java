package com.es.phoneshop.web.service.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.impl.CartServiceImpl;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.web.service.SessionService;
import lombok.SneakyThrows;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpSession;

import static com.es.phoneshop.constant.SessionAttributeConstants.CART_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.UserMessageConstants.ADD_TO_CART_SUCCESS_MESSAGE;
import static com.es.phoneshop.constant.UserMessageConstants.REMOVE_FROM_CART_SUCCESS_MESSAGE;

public class SessionServiceImpl implements SessionService {
    private final CartService cartService;

    private SessionServiceImpl() {
        cartService = CartServiceImpl.getInstance();
    }

    public static SessionServiceImpl getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public String updateCurrentCart(HttpSession session, Product product, int quantity) {
        synchronized (WebUtils.getSessionMutex(session)) {
            Cart cart = getCart(session);
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            if (quantity > 0) {
                cartService.update(cart, product, quantity);
                return ADD_TO_CART_SUCCESS_MESSAGE;
            } else if (cartService.delete(cart, product)) {
                return REMOVE_FROM_CART_SUCCESS_MESSAGE;
            }
            return "";
        }
    }

    @Override
    public Cart getCart(HttpSession session) {
        return castAttribute(session.getAttribute(CART_SESSION_ATTRIBUTE), Cart.class);
    }

    @SneakyThrows
    @Override
    public <T> T castAttribute(Object attribute, Class<T> clazz) {
        if (attribute != null) {
            return clazz.cast(attribute);
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    private static class LazySingletonHolder {
        public static final SessionServiceImpl HOLDER_INSTANCE = new SessionServiceImpl();
    }
}
