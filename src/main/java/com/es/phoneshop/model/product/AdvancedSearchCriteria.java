package com.es.phoneshop.model.product;

import com.es.phoneshop.enumeration.SearchMode;

import java.math.BigDecimal;

public class AdvancedSearchCriteria {
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final String query;
    private final SearchMode searchMode;

    public AdvancedSearchCriteria(BigDecimal minPrice, BigDecimal maxPrice, String query, SearchMode searchMode) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.query = query;
        this.searchMode = searchMode;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public String getQuery() {
        return query;
    }

    public SearchMode getSearchMode() {
        return searchMode;
    }
}
