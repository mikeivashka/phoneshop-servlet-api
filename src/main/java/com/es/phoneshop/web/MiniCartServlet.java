package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.web.service.SessionService;
import com.es.phoneshop.web.service.impl.SessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.es.phoneshop.constant.SessionAttributeConstants.CART_SESSION_ATTRIBUTE;

public class MiniCartServlet extends HttpServlet {
    private SessionService sessionService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sessionService = SessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = sessionService.castAttribute(req.getSession(true).getAttribute(CART_SESSION_ATTRIBUTE), Cart.class);
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/pages/miniCart.jsp").include(req, resp);
    }
}
