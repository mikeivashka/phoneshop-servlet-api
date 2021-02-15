package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.RecentProductsService;
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
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.es.phoneshop.web.ProductDetailsPageServlet.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest extends Assert {
    private static final String INVALID_PRODUCT_ID = "a";
    private static final String INVALID_QUANTITY = "a";
    private static final int PRODUCT_STOCK = 2;
    @InjectMocks
    @Spy
    private ProductDetailsPageServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Spy
    private ProductService service;
    @Spy
    private RecentProductsService recentProductsService;
    @Mock
    private Product product;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Mock
    private CartService cartService;

    @SneakyThrows
    @Before
    public void setUp() {
        long productId = 0L;
        when(request.getSession(true)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter(QUANTITY_PARAMETER_NAME)).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(service.findById(anyLong())).thenReturn(Optional.of(product));
        when(session.getAttribute(RECENT_PRODUCTS_ATTRIBUTE)).thenReturn(new LinkedList<Product>());
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

    @SneakyThrows
    @Test
    public void testRecentProductsListIsCreatedIfNull() {
        when(session.getAttribute(RECENT_PRODUCTS_ATTRIBUTE)).thenReturn(null);

        servlet.doGet(request, response);
        verify(session).setAttribute(eq(RECENT_PRODUCTS_ATTRIBUTE), any(List.class));
    }

    @SneakyThrows
    @Test
    public void testDoPostEnoughStock() {
        when(request.getParameter(QUANTITY_PARAMETER_NAME)).thenReturn(String.valueOf(PRODUCT_STOCK));
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + SUCCESS_MESSAGE_PARAMETER_NAME + "=" + ADD_TO_CART_SUCCESS_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostNotEnoughStock() {
        OutOfStockException exception = new OutOfStockException("", PRODUCT_STOCK + 1, PRODUCT_STOCK);
        doThrow(exception).when(cartService).update(any(Cart.class), anyLong(), anyInt());
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + ERROR_MESSAGE_PARAMETER_NAME + "=" + exception.getMessage()));
    }

    @SneakyThrows
    @Test
    public void testDoPostUnparseableQuantity() {
        when(request.getParameter(QUANTITY_PARAMETER_NAME)).thenReturn(INVALID_QUANTITY);
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + ERROR_MESSAGE_PARAMETER_NAME + "=" + NOT_A_NUMBER_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostZeroQuantityForProductInCartHasSuccessMessage() {
        when(request.getParameter(QUANTITY_PARAMETER_NAME)).thenReturn(String.valueOf(0));
        when(cartService.delete(any(Cart.class), anyLong())).thenReturn(true);
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + SUCCESS_MESSAGE_PARAMETER_NAME + "=" + REMOVE_FROM_CART_SUCCESS_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostZeroQuantityForProductNotInCartDoesNotHaveSuccessMessage() {
        when(request.getParameter(QUANTITY_PARAMETER_NAME)).thenReturn(String.valueOf(0));
        when(cartService.delete(any(Cart.class), anyLong())).thenReturn(false);
        servlet.doPost(request, response);

        verify(response, never()).sendRedirect(contains(REMOVE_FROM_CART_SUCCESS_MESSAGE));
    }

}
