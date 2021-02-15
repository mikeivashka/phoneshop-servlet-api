package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.util.*;

public class Cart implements Serializable {
    private Deque<CartItem> items;

    public Cart() {
        this.items = new ArrayDeque<>();
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
