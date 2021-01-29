package com.es.phoneshop.model.product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

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
}
