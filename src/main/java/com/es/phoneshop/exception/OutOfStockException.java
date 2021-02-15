package com.es.phoneshop.exception;

public class OutOfStockException extends RuntimeException {
    private final String productDescription;
    private final int requiredStock;
    private final int availableStock;

    public OutOfStockException(String productDescription, int requiredStock, int availableStock) {
        this.productDescription = productDescription;
        this.requiredStock = requiredStock;
        this.availableStock = availableStock;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getRequiredStock() {
        return requiredStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    @Override
    public String getMessage() {
        return "Not enough stock for product " + productDescription +
                ". Required: " + requiredStock +
                ", available: " + availableStock;
    }
}
