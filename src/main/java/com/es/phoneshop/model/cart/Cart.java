package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Cart implements Serializable {
    private Deque<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalPrice;

    public Cart() {
        this.items = new ArrayDeque<>();
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Deque<CartItem> getItems() {
        return items;
    }

    public void setItems(Deque<CartItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(items, cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(items);
    }
}
