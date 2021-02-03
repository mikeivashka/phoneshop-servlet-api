package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.enumeration.SortField;
import com.es.phoneshop.enumeration.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortStrategyProvider;

import java.util.Comparator;

public class ProductSortStrategyProvider implements SortStrategyProvider<Product> {
    private final SortField field;
    private final SortOrder order;

    public ProductSortStrategyProvider(SortField field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    public static ProductSortStrategyProvider empty() {
        return new ProductSortStrategyProvider(SortField.PRICE, SortOrder.NONE);
    }

    @Override
    public Comparator<Product> getComparator() {
        if (!isSortRequired()) {
            return (o1, o2) -> 0;
        }
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (field == SortField.DESCRIPTION) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (order == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    @Override
    public boolean isSortRequired() {
        return order != SortOrder.NONE;
    }
}
