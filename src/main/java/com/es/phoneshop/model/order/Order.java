package com.es.phoneshop.model.order;

import com.es.phoneshop.model.generic.Id;
import com.es.phoneshop.model.cart.Cart;

import java.math.BigDecimal;
import java.util.UUID;

public class Order implements Id<Long> {
    private final Cart cart;
    private final CustomerDetails customerDetails;
    private final OrderDetails orderDetails;
    private BigDecimal totalPrice;
    private Long id;
    private UUID secureId;

    public Order(Cart cart, CustomerDetails customerDetails, OrderDetails orderDetails) {
        this.cart = new Cart(cart);
        this.customerDetails = customerDetails;
        this.orderDetails = orderDetails;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UUID getSecureId() {
        return secureId;
    }

    public void setSecureId(UUID secureId) {
        this.secureId = secureId;
    }
}
