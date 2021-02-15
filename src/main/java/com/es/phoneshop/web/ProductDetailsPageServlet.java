package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.impl.CartServiceImpl;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.RecentProductsService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import com.es.phoneshop.model.product.impl.RecentProductsServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class ProductDetailsPageServlet extends HttpServlet {
    public static final String CART_ATTRIBUTE = "cart";
    public static final String RECENT_PRODUCTS_ATTRIBUTE = "recentProducts";
    public static final String ADD_TO_CART_SUCCESS_MESSAGE = "Successfully added to cart";
    public static final String REMOVE_FROM_CART_SUCCESS_MESSAGE = "Successfully removed from cart";
    public static final String QUANTITY_PARAMETER_NAME = "quantity";
    public static final String SUCCESS_MESSAGE_PARAMETER_NAME = "successMessage";
    public static final String ERROR_MESSAGE_PARAMETER_NAME = "errorMessage";
    public static final String NOT_A_NUMBER_MESSAGE = "Not a number";
    private ProductService productService;
    private CartService cartService;
    private RecentProductsService recentProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getInstance();
        cartService = CartServiceImpl.getInstance();
        recentProductsService = RecentProductsServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            StringBuilder dispatcherPath = new StringBuilder("/WEB-INF/pages/");
            req.getSession(true).setAttribute(CART_ATTRIBUTE, extractCartFromSession(req.getSession()));
            productService.findById(extractProductIdFromRequest(req)).ifPresentOrElse(
                    product -> {
                        addToRecentlyViewedProducts(req.getSession(), product);
                        req.setAttribute("product", product);
                        dispatcherPath.append("productDetails.jsp");
                    },
                    () -> dispatcherPath.append("productNotFound.jsp")
            );
            req.getRequestDispatcher(dispatcherPath.toString()).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder urlPostfix = new StringBuilder();
        try {
            NumberFormat numberFormat = NumberFormat.getIntegerInstance(req.getLocale());
            int quantity = numberFormat.parse(req.getParameter(QUANTITY_PARAMETER_NAME)).intValue();
            urlPostfix.append("?" + SUCCESS_MESSAGE_PARAMETER_NAME + "=");
            Cart cart = extractCartFromSession(req.getSession());
            long productId = extractProductIdFromRequest(req);
            if (quantity > 0) {
                cartService.update(cart, productId, quantity);
                urlPostfix.append(ADD_TO_CART_SUCCESS_MESSAGE);
            } else if (cartService.delete(cart, productId)) {
                urlPostfix.append(REMOVE_FROM_CART_SUCCESS_MESSAGE);
            }
        } catch (OutOfStockException e) {
            urlPostfix.append("?" + ERROR_MESSAGE_PARAMETER_NAME + "=").append(e.getMessage());
        } catch (ParseException e) {
            urlPostfix.append("?" + ERROR_MESSAGE_PARAMETER_NAME + "=").append(NOT_A_NUMBER_MESSAGE);
        } finally {
            resp.sendRedirect(req.getRequestURI() + urlPostfix);
        }

    }

    private Cart extractCartFromSession(HttpSession session) {
        Object currentCart = session.getAttribute(CART_ATTRIBUTE);
        return (currentCart != null) ? (Cart) currentCart : new Cart();
    }

    private Long extractProductIdFromRequest(HttpServletRequest req) {
        return Long.valueOf(req.getPathInfo().substring(1));
    }

    private void addToRecentlyViewedProducts(HttpSession session, Product product) {
        Object recentProductsAttribute = session.getAttribute(RECENT_PRODUCTS_ATTRIBUTE);
        List<Product> recentProducts;
        if (recentProductsAttribute != null) {
            recentProducts = (List<Product>) recentProductsAttribute;
        } else {
            recentProducts = new LinkedList<>();
        }
        recentProductsService.addToRecentProducts(recentProducts, product);
        session.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentProducts);
    }
}

