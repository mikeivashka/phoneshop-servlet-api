package com.es.phoneshop.model.order.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.CustomerDetails;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDetails;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.product.ProductTestCommonConditions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;

import static org.junit.Assert.*;

public class OrderServiceImplTest extends ProductTestCommonConditions {
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private Order order;
    private CustomerDetails customerDetails;
    private OrderDetails orderDetails;
    private Cart cart;

    @Before
    public void setup() {
        cart = new Cart();
        cart.setItems(new ArrayDeque<>(Arrays.asList(new CartItem(product1, 1), new CartItem(product2, 1))));
        cart.setTotalPrice(product1.getPrice().add(product2.getPrice()));
        customerDetails = new CustomerDetails();
        orderDetails = new OrderDetails();
        order = new Order(cart, customerDetails, orderDetails);
    }

    @Test
    public void testCreateOrderDeliveryPriceIsSet() {
        orderDetails.setDeliveryCost(null);
        OrderDetails updatedOrderDetails = orderService.createOrder(cart, customerDetails, orderDetails).getOrderDetails();

        assertNotNull(updatedOrderDetails.getDeliveryCost());
    }

    @Test
    public void testCreateOrderTotalPriceIsEqualToCartAndDeliveryCostSum() {
        Order order1 = orderService.createOrder(cart, customerDetails, orderDetails);

        assertEquals(order1.getOrderDetails().getDeliveryCost().add(cart.getTotalPrice()), order1.getTotalPrice());
    }

    @Test
    public void testCreateOrderDetailsIsCreatedIfAbsent() {
        Order order1 = orderService.createOrder(cart, customerDetails, null);

        assertNotNull(order1.getOrderDetails());
    }

    @Test
    public void testCreateOrderCustomerDetailsNotCreatedIfAbsent() {
        Order order1 = orderService.createOrder(cart, null, orderDetails);

        assertNull(order1.getCustomerDetails());
    }

    @Test
    public void testCreateOrderCartIsCloned() {
        Order order1 = orderService.createOrder(cart, customerDetails, orderDetails);

        assertNotSame(cart, order1.getCart());
        assertNotSame(cart.getItems(), order1.getCart().getItems());
        assertArrayEquals(cart.getItems().toArray(), order1.getCart().getItems().toArray());
    }

    @Test
    public void testPlaceOrderSecureIdIsCreated() {
        order.setSecureId(null);
        orderService.placeOrder(order);

        assertNotNull(order.getSecureId());
    }

    @Test(expected = NullPointerException.class)
    public void testPlaceOrderNullCausesNPE() {
        orderService.placeOrder(null);
    }
}