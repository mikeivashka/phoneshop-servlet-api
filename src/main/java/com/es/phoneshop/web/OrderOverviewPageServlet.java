package com.es.phoneshop.web;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import com.es.phoneshop.model.order.impl.ArrayListOrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.es.phoneshop.constant.RequestParameterConstants.ORDER_REQUEST_PARAMETER;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String secureOrderId = req.getPathInfo().substring(1);
        Order order = orderDao.getOrderBySecureId(UUID.fromString(secureOrderId)).orElseThrow(OrderNotFoundException::new);
        req.setAttribute(ORDER_REQUEST_PARAMETER, order);
        req.getRequestDispatcher("/WEB-INF/pages/orderOverview.jsp").forward(req, resp);
    }
}
