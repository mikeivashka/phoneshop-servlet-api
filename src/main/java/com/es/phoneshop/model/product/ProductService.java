package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.impl.ProductSortStrategyProvider;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    default List<Product> findAll() {
        return findByQuery("", ProductSortStrategyProvider.empty());
    }

    default List<Product> findAll(SortStrategyProvider<Product> sortStrategy) {
        return findByQuery("", sortStrategy);
    }

    default List<Product> findByQuery(String query) {
        return findByQuery(query, ProductSortStrategyProvider.empty());
    }

    List<Product> findByQuery(String query, SortStrategyProvider<Product> sortStrategy);

    List<Product> findByAdvancedSearchCriteria(AdvancedSearchCriteria criteria);

    Optional<Product> findById(Long id);

    void add(Product product);

    void delete(long id);

    SortStrategyProvider<Product> resolveSortStrategy(String sortFieldParam, String sortOrderParam);

}
