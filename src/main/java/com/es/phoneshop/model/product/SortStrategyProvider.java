package com.es.phoneshop.model.product;

import java.util.Comparator;

public interface SortStrategyProvider<T> {
    Comparator<T> getComparator();

    boolean isSortRequired();
}
