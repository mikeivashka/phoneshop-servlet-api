package com.es.phoneshop.web;

import com.es.phoneshop.model.product.PriceHistoryEvent;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

public class DemoDataServletContextListener implements ServletContextListener {

    private final ProductService productService;

    public DemoDataServletContextListener() {
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean insertDemoData = Boolean.parseBoolean(event.getServletContext().getInitParameter("insertDemoData"));
        if (insertDemoData) {
            saveSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        productService.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(400), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9)), new PriceHistoryEvent(BigDecimal.valueOf(800), LocalDate.of(2014, 4, 4))))));
        productService.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(600), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9)), new PriceHistoryEvent(BigDecimal.valueOf(900), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(800), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9)), new PriceHistoryEvent(BigDecimal.valueOf(1000), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("iphone", "Apple iPhone", new BigDecimal(600), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(400), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(700), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(700), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(200), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(300), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(400), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(250), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(350), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(300), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(150), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("palmp", "Palm Pixi", new BigDecimal(100), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("simc56", "Siemens C56", new BigDecimal(150), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(230), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("simc61", "Siemens C61", new BigDecimal(200), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(250), LocalDate.of(2009, 9, 9))))));
        productService.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(300), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", new ArrayList<>(Arrays.asList(new PriceHistoryEvent(BigDecimal.valueOf(100), LocalDate.of(2001, 1, 1)), new PriceHistoryEvent(BigDecimal.valueOf(200), LocalDate.of(2009, 9, 9))))));
    }
}
