package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String rawId = req.getPathInfo().substring(1);
            StringBuilder dispatcherPath = new StringBuilder("/WEB-INF/pages/");
            service.findById(Long.valueOf(rawId)).ifPresentOrElse(
                    product -> {
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
}
