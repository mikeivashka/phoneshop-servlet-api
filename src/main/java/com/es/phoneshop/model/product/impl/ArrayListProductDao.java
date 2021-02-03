package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortStrategyProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private final List<Product> products;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final AtomicLong maxId = new AtomicLong();

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static ArrayListProductDao getInstance() {
        return LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        if (id == null) {
            throw new NullPointerException();
        }
        readWriteLock.readLock().lock();
        Optional<Product> product = products.stream()
                .filter(p -> id.equals(p.getId()))
                .findAny();
        readWriteLock.readLock().unlock();
        return product;
    }

    @Override
    public List<Product> findProducts(String query, SortStrategyProvider<Product> sortStrategy) {
        readWriteLock.readLock().lock();
        List<Product> filteredProducts = products.stream()
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getStock() > 0)
                .filter(p -> query == null || query.isEmpty() || getProductRelevancePoints(p, query) > 0)
                .collect(Collectors.toList());
        readWriteLock.readLock().unlock();
        performSorting(filteredProducts, query, sortStrategy);
        return filteredProducts;
    }


    @Override
    public void save(Product product) {
        if (product.getId() != null && this.getProduct(product.getId()).isPresent()) {
            this.delete(product.getId());
        } else {
            product.setId(maxId.getAndIncrement());
        }
        readWriteLock.writeLock().lock();
        products.add(product);
        readWriteLock.writeLock().unlock();
    }

    @Override
    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        products.removeIf(p -> p.getId().equals(id));
        readWriteLock.writeLock().unlock();
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
