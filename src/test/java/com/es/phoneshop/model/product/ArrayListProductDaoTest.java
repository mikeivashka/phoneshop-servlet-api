package com.es.phoneshop.model.product;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Currency;


@RunWith(JUnitParamsRunner.class)
public class ArrayListProductDaoTest extends Assert {
    private ProductDao productDao;
    private Product product;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        productDao.findProducts()
                .forEach(p -> productDao.delete(p.getId()));
        product = new Product("first", "First test product", new BigDecimal(1), Currency.getInstance("USD"), 1, "https://sampleURL1.com");
    }

    @Parameterized.Parameters

    @Test
    public void testSaveProduct() {
        productDao.save(product);
        assertTrue(productDao.findProducts().contains(product));
    }

    @Test
    public void testGetProduct() {
        productDao.save(product);
        assertEquals(product, productDao.getProduct(product.getId()).get());
    }

    @Test
    public void testFindProductsIsEmpty() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testDeleteProduct() {
        productDao.save(product);
        productDao.delete(product.getId());
        assertFalse(productDao.getProduct(product.getId()).isPresent());
    }

    @Test
    public void testProductGetsIdAfterSave() {
        productDao.save(product);
        assertNotNull(product.getId());
    }

    @Parameters(method = "provideZeroStockOrNullPrice")
    @Test
    public void testProductsWithNullPriceOrZeroStockAreIgnored(BigDecimal price, int stock) {
        product.setPrice(price);
        product.setStock(stock);
        productDao.save(product);
        assertFalse(productDao.findProducts().contains(product));
        assertTrue(productDao.getProduct(product.getId()).isPresent());
    }

    private Object[] provideZeroStockOrNullPrice() {
        return new Object[]{
                new Object[]{null, 1},
                new Object[]{new BigDecimal(0), 0}
        };
    }
}
