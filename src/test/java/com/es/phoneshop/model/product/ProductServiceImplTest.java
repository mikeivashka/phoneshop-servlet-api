package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProductServiceImplTest extends ProductTestCommonConditions {
    private final ProductService service = ProductServiceImpl.getInstance();

    @Before
    public void setup() {
        service.findAll().stream()
                .map(Product::getId)
                .forEach(service::delete);
    }

    @Test
    public void testAddProduct() {
        service.add(product1);
        assertEquals(1, service.findAll().size());
        assertTrue(service.findById(product1.getId()).isPresent());
    }

    @Test
    public void testDeleteExistingObject() {
        service.add(product1);
        Long id = product1.getId();
        service.delete(id);
        assertEquals(Optional.empty(), service.findById(id));
    }

    @Test
    public void testSearchFullMatch() {
        service.add(product1);
        service.add(product2);
        assertTrue(service.findByQuery(product1.getDescription()).contains(product1));
    }

    @Test
    public void testDeleteNotExistingObjectMakesNoChange() {
        service.add(product1);
        Long id = product1.getId();
        service.delete(id);
        List<Product> productsBefore = service.findAll();
        service.delete(id);
        assertEquals(productsBefore, service.findAll());
    }

    @Test
    public void testFindAllCallEqualToFindByQueryWithEmptyArgs() {
        service.add(product1);
        service.add(product2);
        assertEquals(service.findAll(), service.findByQuery(""));
    }

    @Test
    public void testProductPriceHistoryIsSyncWithCurrentPriceOnAdd() {
        service.add(product1);
        BigDecimal previousPrice = product1.getPrice();
        product1.setPrice(previousPrice.add(BigDecimal.ONE));
        service.add(product1);
        List<PriceHistoryEvent> priceHistory = product1.getPriceHistory();
        assertEquals(product1.getPrice(), priceHistory.get(priceHistory.size() - 1).getNewPrice());
    }

    @Test
    public void testProductPriceHistoryIsNotSyncOnAddIfPriceIsNull() {
        List<PriceHistoryEvent> previousPriceHistory = product1.getPriceHistory();
        service.add(product1);
        product1.setPrice(null);
        service.add(product1);
        assertEquals(previousPriceHistory, product1.getPriceHistory());
    }

    @Test
    public void testProductPriceHistoryIsCreatedOnAdd() {
        product1.setPriceHistory(null);
        service.add(product1);
        assertNotNull(product1.getPriceHistory());
        assertEquals(1, product1.getPriceHistory().size());
        assertEquals(product1.getPrice(), product1.getPriceHistory().get(0).getNewPrice());
    }

    @Test
    public void testProductPriceHistoryNotUpdatedOnAddIfPriceIsSame() {
        product1.getPriceHistory().add(new PriceHistoryEvent(product1.getPrice(), LocalDate.now()));
        List<PriceHistoryEvent> previousPriceHistory = product1.getPriceHistory();
        service.add(product1);
        assertEquals(previousPriceHistory, product1.getPriceHistory());
    }
}
