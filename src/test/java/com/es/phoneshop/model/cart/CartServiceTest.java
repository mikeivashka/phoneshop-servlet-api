package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.impl.CartServiceImpl;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.ProductTestCommonConditions;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CartServiceTest extends ProductTestCommonConditions {
    public static final int QUANTITY_ONE = 1;
    public static final int QUANTITY_TWO = 2;
    private final ProductService productService = ProductServiceImpl.getInstance();
    private final CartService cartService = CartServiceImpl.getInstance();
    private final Cart cart = new Cart();

    @Before
    public void setup() {
        product1.setStock(QUANTITY_TWO);
        productService.add(product1);
    }

    @Test
    public void testAddToEmptyCart() {
        cartService.update(cart, product1, 1);
        assertTrue(cart.getItems().stream()
                .anyMatch(cartItem -> cartItem.getProduct().equals(product1)));
    }

    @Test
    public void testProductWithZeroQuantityIsDeleted() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cartService.update(cart, product1, 0);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testDeleteFromCart() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cartService.delete(cart, product1);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testDeleteNotExistingProductMakesNoChange() {
        cartService.delete(cart, product2);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testUpdateOnExistingProductChangesQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cartService.update(cart, product1, QUANTITY_TWO);
        assertEquals(1L, cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product1))
                .count()
        );
        assertEquals(QUANTITY_TWO, cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product1))
                .findAny().get().getQuantity()
        );
    }

    @Test
    public void testUpdateRecalculatesTotalPrice() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalPrice(product1.getPrice());

        cartService.update(cart, product1, QUANTITY_TWO);

        assertEquals(product1.getPrice().multiply(BigDecimal.valueOf(QUANTITY_TWO)), cart.getTotalPrice());
    }

    @Test
    public void testDeleteRecalculatesTotalPrice() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalPrice(product1.getPrice());

        cartService.delete(cart, product1);

        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    public void testUpdateRecalculatesTotalQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalQuantity(QUANTITY_ONE);

        cartService.update(cart, product1, QUANTITY_TWO);
        assertEquals(QUANTITY_TWO, cart.getTotalQuantity());
    }

    @Test
    public void testDeleteRecalculatesTotalQuantity() {
        cart.getItems().add(new CartItem(product1, QUANTITY_ONE));
        cart.setTotalQuantity(QUANTITY_ONE);

        cartService.delete(cart, product1);

        assertEquals(0, cart.getTotalQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateNotEnoughStock() {
        cartService.update(cart, product1, product1.getStock() + 1);
    }

}
