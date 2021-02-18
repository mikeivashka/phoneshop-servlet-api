package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import com.es.phoneshop.web.service.SessionService;
import com.es.phoneshop.web.service.impl.SessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.SessionAttributeConstants.CART_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.UserMessageConstants.NOT_A_NUMBER_MESSAGE;
import static com.es.phoneshop.constant.UserMessageConstants.UPDATE_CART_SUCCESS_MESSAGE;

public class CartPageServlet extends HttpServlet {
    private SessionService sessionService;
    private ProductService productService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sessionService = SessionServiceImpl.getInstance();
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = sessionService.castAttribute(req.getSession(true).getAttribute(CART_SESSION_ATTRIBUTE), Cart.class);
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(req.getLocale());
        List<Long> products = Arrays.stream(req.getParameterValues(PRODUCT_ID_REQUEST_PARAMETER))
                .map(Long::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
        String[] quantities = req.getParameterValues(QUANTITY_REQUEST_PARAMETER);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < products.size(); i++) {
            try {
                sessionService.updateCurrentCart(req.getSession(),
                        productService.findById(products.get(i)).orElseThrow(NoSuchElementException::new),
                        numberFormat.parse(quantities[i]).intValue()
                );
            } catch (ParseException e) {
                errors.put(products.get(i), NOT_A_NUMBER_MESSAGE);
            } catch (OutOfStockException e) {
                errors.put(products.get(i), e.getMessage());
            } catch (NoSuchElementException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        if (errors.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart?" + SUCCESS_MESSAGE_REQUEST_PARAMETER + "=" + UPDATE_CART_SUCCESS_MESSAGE);
        } else {
            req.setAttribute("errors", errors);
            doGet(req, resp);
        }
    }
}
