package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.RecentProductsService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import com.es.phoneshop.model.product.impl.RecentProductsServiceImpl;
import com.es.phoneshop.web.service.SessionService;
import com.es.phoneshop.web.service.impl.SessionServiceImpl;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.SessionAttributeConstants.CART_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.SessionAttributeConstants.RECENT_PRODUCTS_SESSION_ATTRIBUTE;
import static com.es.phoneshop.constant.UserMessageConstants.NOT_A_NUMBER_MESSAGE;
import static com.es.phoneshop.constant.UserMessageConstants.UNKNOWN_ERROR_MESSAGE;


public class ProductDetailsPageServlet extends HttpServlet {
    private ProductService productService;
    private RecentProductsService recentProductsService;
    private SessionService sessionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getInstance();
        recentProductsService = RecentProductsServiceImpl.getInstance();
        sessionService = SessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StringBuilder dispatcherPath = new StringBuilder("/WEB-INF/pages/");
            HttpSession session = req.getSession(true);
            synchronized (WebUtils.getSessionMutex(session)) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, extractCartFromSession(req.getSession()));
                extractProductFromRequest(req).ifPresentOrElse(
                        product -> {
                            addToRecentlyViewedProducts(req.getSession(), product);
                            req.setAttribute("product", product);
                            dispatcherPath.append("productDetails.jsp");
                        },
                        () -> dispatcherPath.append("productNotFound.jsp")
                );
            }
            req.getRequestDispatcher(dispatcherPath.toString()).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder redirectUrl = new StringBuilder(req.getRequestURI()).append("?");
        try {
            NumberFormat numberFormat = NumberFormat.getIntegerInstance(req.getLocale());
            int quantity = numberFormat.parse(req.getParameter(QUANTITY_REQUEST_PARAMETER)).intValue();
            HttpSession session = req.getSession(true);
            Product product = extractProductFromRequest(req).orElseThrow(NoSuchElementException::new);
            String successMessage = sessionService.updateCurrentCart(session, product, quantity);
            redirectUrl.append(SUCCESS_MESSAGE_REQUEST_PARAMETER + "=").append(successMessage);
        } catch (NoSuchElementException e) {
            redirectUrl.append(ERROR_MESSAGE_REQUEST_PARAMETER + "=").append(UNKNOWN_ERROR_MESSAGE);
        } catch (OutOfStockException e) {
            redirectUrl.append(ERROR_MESSAGE_REQUEST_PARAMETER + "=").append(e.getMessage());
        } catch (ParseException e) {
            redirectUrl.append(ERROR_MESSAGE_REQUEST_PARAMETER + "=").append(NOT_A_NUMBER_MESSAGE);
        } finally {
            resp.sendRedirect(redirectUrl.toString());
        }

    }

    private Cart extractCartFromSession(HttpSession session) {
        Object currentCart = session.getAttribute(CART_SESSION_ATTRIBUTE);
        return sessionService.castAttribute(currentCart, Cart.class);
    }

    private Optional<Product> extractProductFromRequest(HttpServletRequest req) {
        return productService.findById(Long.valueOf(req.getPathInfo().substring(1)));
    }

    private void addToRecentlyViewedProducts(HttpSession session, Product product) {
        Object recentProductsAttribute = session.getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE);
        Deque<Product> recentProducts = sessionService.castAttribute(recentProductsAttribute, ArrayDeque.class);
        recentProductsService.addToRecentProducts(recentProducts, product);
        session.setAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE, recentProducts);
    }
}
