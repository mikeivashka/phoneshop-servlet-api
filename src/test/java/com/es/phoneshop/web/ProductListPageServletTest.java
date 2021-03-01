package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.ProductTestCommonConditions;
import com.es.phoneshop.web.service.SessionService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Optional;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.SessionAttributeConstants.CART_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.UserMessageConstants.UPDATE_CART_SUCCESS_MESSAGE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest extends ProductTestCommonConditions {
    @InjectMocks
    @Spy
    private ProductListPageServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig servletConfig;

    @SneakyThrows
    @Before
    public void setup() {
        servlet.init(servletConfig);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
//        when(request.getRequestURI()).thenReturn("");
//        when(request.getLocale()).thenReturn(Locale.ROOT);
//        when(request.getParameter(PRODUCT_ID_REQUEST_PARAMETER)).thenReturn("1");
//        when(request.getParameter(QUANTITY_REQUEST_PARAMETER)).thenReturn("1");
//        when(request.getSession(anyBoolean())).thenReturn(session);
//        when(productService.findById(anyLong())).thenReturn(Optional.of(product));
//        when(session.getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(new Cart());
    }

    @SneakyThrows
    @Test
    public void testDoGet() {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @SneakyThrows
    @Test
    public void testRequestContainsProducts() {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("products"), anyList());
    }

//    @SneakyThrows
//    @Test
//    public void testDoPost() {
//        when(sessionService.updateCurrentCart(any(), eq(product1), anyInt())).thenReturn(UPDATE_CART_SUCCESS_MESSAGE);
//        servlet.doPost(request, response);
//
//        verify(request).getSession(true);
//        verify(productService).findById(anyLong()).orElseThrow(any());
//
//        verify(response).sendRedirect(contains(SUCCESS_MESSAGE_REQUEST_PARAMETER));
//    }
}
