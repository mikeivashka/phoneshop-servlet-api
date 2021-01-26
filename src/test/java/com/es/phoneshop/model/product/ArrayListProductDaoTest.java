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
        product = new Product("simsxg76", "Siemens SXG75", new BigDecimal(150), Currency.getInstance("USD"), 40, "sampleURL");
    }

    @Test
    public void testGetProduct() {
        Product productToGet = productDao.findProducts().get(0);
        assertEquals(productToGet, productDao.getProduct(productToGet.getId()).get());
    }

    @Test
    public void testFindProductsNotEmpty() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testDeleteProduct() {
        Long productToDeleteId = productDao.findProducts().get(0).getId();
        productDao.delete(productToDeleteId);
        assertFalse(productDao.getProduct(productToDeleteId).isPresent());
    }

    @Test
    public void testSaveProduct() {
        productDao.save(product);
        assertTrue(productDao.findProducts().contains(product));
    }

    @Test
    public void testProductGetsIdAfterSave() {
        assertNull(product.getId());
        productDao.save(product);
        assertNotNull(product.getId());
    }
}
