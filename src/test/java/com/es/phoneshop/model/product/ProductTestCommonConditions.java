package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

public abstract class ProductTestCommonConditions {
    protected Product product1;
    protected Product product2;

    {
        product1 = new Product("first", "First test product", new BigDecimal(300), Currency.getInstance("USD"), 1, "https://sampleURL1.com", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9)))));
        product2 = new Product("second", "Second test product", new BigDecimal(50), Currency.getInstance("USD"), 2, "https://sampleURL2.com", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(20), LocalDate.of(2001, 9, 11)), new PriceHistoryEvent(BigDecimal.valueOf(50), LocalDate.of(2010, 9, 9)))));
    }
}
