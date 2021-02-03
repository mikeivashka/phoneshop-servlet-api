package com.es.phoneshop.web;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    private final ProductListPageServlet servlet = new ProductListPageServlet();
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
}
