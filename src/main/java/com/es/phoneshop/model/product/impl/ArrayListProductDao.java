package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.model.generic.AbstractListDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortStrategyProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractListDao<Product, Long> implements ProductDao {
    private final AtomicLong maxId = new AtomicLong();

    private ArrayListProductDao() {
        super();
    }

    public static ArrayListProductDao getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public List<Product> findProducts(String query, SortStrategyProvider<Product> sortStrategy) {
        readWriteLock.readLock().lock();
        List<Product> filteredProducts = items.stream()
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getStock() > 0)
                .filter(p -> query == null || query.isEmpty() || getProductRelevancePoints(p, query) > 0)
                .collect(Collectors.toList());
        readWriteLock.readLock().unlock();
        performSorting(filteredProducts, query, sortStrategy);
        return filteredProducts;
    }

    @Override
    protected Long getNextId() {
        return maxId.getAndIncrement();
    }

    @Override
    protected List<Product> getContainer() {
        return new ArrayList<>();
    }

    private long getProductRelevancePoints(Product product, String query) {
        return Arrays.stream(query.split("[\\W]"))
                .filter(s -> product.getDescription().toLowerCase().contains(s.toLowerCase()))
                .count();
    }

    private void performSorting(List<Product> products, String query, SortStrategyProvider<Product> sortStrategy) {
        if (sortStrategy.isSortRequired()) {
            products.sort(sortStrategy.getComparator());
        } else if (query != null && !query.isBlank()) {
            sortByRelevance(products, query);
        }
    }

    private void sortByRelevance(List<Product> products, String query) {
        products
                .sort((p1, p2) -> (int) (getProductRelevancePoints(p2, query) - getProductRelevancePoints(p1, query)));
    }

    private static class LazySingletonHolder {
        public static final ArrayListProductDao HOLDER_INSTANCE = new ArrayListProductDao();
    }
}
