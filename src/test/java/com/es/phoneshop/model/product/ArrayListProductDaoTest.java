package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.impl.ArrayListProductDao;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class ArrayListProductDaoTest extends ProductTestCommonConditions {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        productDao.findProducts()
                .forEach(p -> productDao.delete(p.getId()));
    }

    @Test
    public void testSaveProduct() {
        productDao.save(product1);
        assertTrue(productDao.findProducts().contains(product1));
    }

    @Test
    public void testGetProduct() {
        productDao.save(product1);
        assertEquals(product1, productDao.getProduct(product1.getId()).get());
    }

    @Test
    public void testFindProductsWithEmptyQuery() {
        productDao.save(product1);
        assertEquals(productDao.findProducts(), productDao.findProducts(""));
    }

    @Test
    public void testFindProductsWithDescriptionFullMatchesQuery() {
        productDao.save(product1);
        productDao.save(product2);
        assertTrue(productDao.findProducts(product1.getDescription()).contains(product1));
    }

    @Test
    public void testFindProductsIsEmpty() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testDeleteProduct() {
        productDao.save(product1);
        productDao.delete(product1.getId());
        assertFalse(productDao.getProduct(product1.getId()).isPresent());
    }

    @Test
    public void testProductGetsIdAfterSave() {
        productDao.save(product1);
        assertNotNull(product1.getId());
    }

    @Test
    public void testProductIsUpdatedOnSaveIfIdIsUsed() {
        productDao.save(product1);
        Long firstProductId = product1.getId();
        product2.setId(firstProductId);
        productDao.save(product2);
        assertEquals(product2, productDao.getProduct(firstProductId).get());
    }

    @Parameters(method = "provideZeroStockOrNullPrice")
    @Test
    public void testProductsWithNullPriceOrZeroStockAreIgnored(BigDecimal price, int stock) {
        product1.setPrice(price);
        product1.setStock(stock);
        productDao.save(product1);
        assertFalse(productDao.findProducts().contains(product1));
        assertTrue(productDao.getProduct(product1.getId()).isPresent());
    }

    private Object[] provideZeroStockOrNullPrice() {
        return new Object[]{
                new Object[]{null, 1},
                new Object[]{new BigDecimal(0), 0}
        };
    }
}
