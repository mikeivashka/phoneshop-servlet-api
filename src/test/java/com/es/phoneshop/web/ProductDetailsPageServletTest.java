package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.RecentProductsService;
import com.es.phoneshop.web.service.SessionService;
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
import java.util.*;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.SessionAttributeConstants.RECENT_PRODUCTS_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.UserMessageConstants.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest extends Assert {
    private static final String INVALID_PRODUCT_ID = "a";
    private static final String INVALID_QUANTITY = "a";
    private static final int PRODUCT_STOCK = 2;
    private static final long productId = 0L;
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
    @Spy
    private SessionService sessionService;
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
        when(request.getSession(true)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(service.findById(anyLong())).thenReturn(Optional.of(product));
        when(session.getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE)).thenReturn(new ArrayDeque<>());
        when(sessionService.castAttribute(any(), eq(ArrayDeque.class))).thenReturn(new ArrayDeque<Product>());
        when(sessionService.castAttribute(any(), eq(Cart.class))).thenReturn(new Cart());
        when(request.getRequestURI()).thenReturn("");
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
        when(session.getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE)).thenReturn(null);

        servlet.doGet(request, response);
        verify(session).setAttribute(eq(RECENT_PRODUCTS_SESSION_ATTRIBUTE), any(ArrayDeque.class));
    }

    @SneakyThrows
    @Test
    public void testDoPostEnoughStock() {
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn(String.valueOf(PRODUCT_STOCK));
        when(sessionService.updateCurrentCart(session, product, PRODUCT_STOCK)).thenReturn(ADD_TO_CART_SUCCESS_MESSAGE);
        servlet.doPost(request, response);

        verify(sessionService).updateCurrentCart(session, product, PRODUCT_STOCK);
        verify(response).sendRedirect(contains("?" + SUCCESS_MESSAGE_REQUEST_PARAMETER + "=" + ADD_TO_CART_SUCCESS_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostNotEnoughStock() {
        OutOfStockException exception = new OutOfStockException("", PRODUCT_STOCK + 1, PRODUCT_STOCK);
        doThrow(exception).when(sessionService).updateCurrentCart(any(), any(), anyInt());
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + ERROR_MESSAGE_REQUEST_PARAMETER + "=" + exception.getMessage()));
    }

    @SneakyThrows
    @Test
    public void testDoPostUnparseableQuantity() {
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn(INVALID_QUANTITY);
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + ERROR_MESSAGE_REQUEST_PARAMETER + "=" + NOT_A_NUMBER_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostZeroQuantityForProductDeletesItFromCart() {
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn(String.valueOf(0));
        servlet.doPost(request, response);

        verify(sessionService).updateCurrentCart(session, product, 0);
    }

    @SneakyThrows
    @Test
    public void testDoPostZeroQuantityForProductInCartHasSuccessRemoveMessage() {
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn(String.valueOf(0));
        when(sessionService.updateCurrentCart(any(), eq(product), eq(0))).thenReturn(REMOVE_FROM_CART_SUCCESS_MESSAGE);
        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("?" + SUCCESS_MESSAGE_REQUEST_PARAMETER + "=" + REMOVE_FROM_CART_SUCCESS_MESSAGE));
    }

    @SneakyThrows
    @Test
    public void testDoPostZeroQuantityForProductNotInCartDoesNotHaveSuccessRemoveMessage() {
        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn(String.valueOf(0));
        servlet.doPost(request, response);

        verify(response, never()).sendRedirect(contains(REMOVE_FROM_CART_SUCCESS_MESSAGE));
    }

}
