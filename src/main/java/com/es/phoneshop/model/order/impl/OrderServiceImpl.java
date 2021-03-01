package com.es.phoneshop.model.order.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.*;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao = ArrayListOrderDao.getInstance();

    private OrderServiceImpl() {
    }

    public static OrderServiceImpl getInstance() {
        return OrderServiceImpl.LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public Order createOrder(Cart cart, CustomerDetails customerDetails, OrderDetails orderDetails) {
        if (orderDetails == null) {
            orderDetails = new OrderDetails();
        }
        Order order = new Order(cart, customerDetails, orderDetails);
        orderDetails.setDeliveryCost(calculateDeliveryCost());
        order.setTotalPrice(cart.getTotalPrice().add(orderDetails.getDeliveryCost()));
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return BigDecimal.valueOf(5);
    }

    private static class LazySingletonHolder {
        public static final OrderServiceImpl HOLDER_INSTANCE = new OrderServiceImpl();
    }
}
