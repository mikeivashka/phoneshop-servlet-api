package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest extends Assert {
    private static final String INVALID_PRODUCT_ID = "a";
    @InjectMocks
    @Spy
    private ProductDetailsPageServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Spy
    private ProductService service;
    @Mock
    private Product product;
    @Mock
    private RequestDispatcher requestDispatcher;

    @SneakyThrows
    @Before
    public void setUp() {
        long productId = 0L;
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(service.findById(anyLong())).thenReturn(Optional.of(product));
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @SneakyThrows
    @Test
    public void testDoGet() {
        servlet.doGet(request, response);

        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @SneakyThrows
    @Test
    public void testNotFoundErrorOnInvalidUrl() {
        when(request.getPathInfo()).thenReturn(INVALID_PRODUCT_ID);
        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @SneakyThrows
    @Test
    public void testNotFoundOnNotExistingProduct() {
        String notExistingIdPathInfo = "/" + Long.MAX_VALUE;
        when(request.getPathInfo()).thenReturn(notExistingIdPathInfo);
        when(service.findById(Long.MAX_VALUE)).thenReturn(Optional.empty());

        servlet.doGet(request, response);
        verify(request).getRequestDispatcher("/WEB-INF/pages/productNotFound.jsp");
    }
}
