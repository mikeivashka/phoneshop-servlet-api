package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;
import com.es.phoneshop.web.service.SessionService;
import com.es.phoneshop.web.service.impl.SessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.NoSuchElementException;

import static com.es.phoneshop.constant.RequestParameterConstants.*;

public class ProductListPageServlet extends AbstractServlet {
    private ProductService productService;
    private SessionService sessionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = ProductServiceImpl.getInstance();
        sessionService = SessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productService.findByQuery(query, productService.resolveSortStrategy(sortField, sortOrder)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder redirectUrl = new StringBuilder(req.getRequestURI() + "?");
        try {
            NumberFormat numberFormat = NumberFormat.getIntegerInstance(req.getLocale());
            Long productId = Long.valueOf(req.getParameter(PRODUCT_ID_REQUEST_PARAMETER));
            int quantity = numberFormat.parse(req.getParameter(QUANTITY_REQUEST_PARAMETER)).intValue();
            String successMessage = sessionService.updateCurrentCart(req.getSession(true),
                    productService.findById(productId).orElseThrow(NoSuchElementException::new),
                    quantity);
            redirectUrl.append(SUCCESS_MESSAGE_REQUEST_PARAMETER + "=").append(successMessage);
        } catch (Exception e) {
            redirectUrl.append(handleDefaultCartModificationException(e));
        } finally {
            resp.sendRedirect(redirectUrl.toString());
        }
    }
}
