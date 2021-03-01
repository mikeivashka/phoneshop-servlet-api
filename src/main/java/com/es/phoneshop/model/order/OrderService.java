package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;

public interface OrderService {
    Order createOrder(Cart cart, CustomerDetails customerDetails, OrderDetails orderDetails);

    default Order createOrder(Cart cart) {
        return createOrder(cart, null, null);
    }

    void placeOrder(Order order);
}
