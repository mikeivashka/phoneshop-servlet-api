package com.es.phoneshop.web.service.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.ProductTestCommonConditions;
import com.es.phoneshop.web.service.SessionService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SessionServiceImplTest extends ProductTestCommonConditions {

    SessionService sessionService = SessionServiceImpl.getInstance();

    @Test
    public void testCastRealCart() {
        Object cart = new Cart();

        assertEquals(cart, sessionService.castAttribute(cart, Cart.class));
    }

    @Test
    public void testCastNullCart() {
        assertNotNull(sessionService.castAttribute(null, Cart.class));
    }
}