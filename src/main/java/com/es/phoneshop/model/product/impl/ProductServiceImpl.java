package com.es.phoneshop.model.product.impl;

import com.es.phoneshop.enumeration.SortField;
import com.es.phoneshop.enumeration.SortOrder;
import com.es.phoneshop.model.product.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = ArrayListProductDao.getInstance();

    private ProductServiceImpl() {
    }

    public static ProductServiceImpl getInstance() {
        return ProductServiceImpl.LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public SortStrategyProvider<Product> resolveSortStrategy(String sortFieldParam, String sortOrderParam) {
        if (sortFieldParam != null && sortOrderParam != null) {
            return new ProductSortStrategyProvider(
                    SortField.valueOf(sortFieldParam.toUpperCase()),
                    SortOrder.valueOf(sortOrderParam.toUpperCase())
            );
        } else {
            return ProductSortStrategyProvider.empty();
        }
    }

    @Override
    public List<Product> findByQuery(String query, SortStrategyProvider<Product> sortStrategy) {
        return productDao.findProducts(query, sortStrategy);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productDao.getProduct(id);
    }

    @Override
    public void add(Product product) {
        syncPriceHistoryWithCurrentPrice(product);
        productDao.save(product);
    }

    @Override
    public void delete(long id) {
        productDao.delete(id);
    }

    private void syncPriceHistoryWithCurrentPrice(Product product) {
        BigDecimal price = product.getPrice();
        if (price == null) {
            return;
        }
        List<PriceHistoryEvent> currentHistory = product.getPriceHistory();
        if (currentHistory == null) {
            List<PriceHistoryEvent> priceHistory = new ArrayList<>();
            priceHistory.add(new PriceHistoryEvent(product.getPrice(), LocalDate.now()));
            product.setPriceHistory(priceHistory);
        } else if (currentHistory.isEmpty() || !currentHistory.get(currentHistory.size() - 1).getNewPrice().equals(price)) {
            product.getPriceHistory().add(new PriceHistoryEvent(product.getPrice(), LocalDate.now()));
        }
    }

    private static class LazySingletonHolder {
        public static final ProductServiceImpl HOLDER_INSTANCE = new ProductServiceImpl();
    }


}
