package com.es.phoneshop.web;

import com.es.phoneshop.enumeration.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.impl.CartServiceImpl;
import com.es.phoneshop.model.order.CustomerDetails;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDetails;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.impl.OrderServiceImpl;
import com.es.phoneshop.web.service.SessionService;
import com.es.phoneshop.web.service.impl.SessionServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.UserMessageConstants.*;

public class CheckoutPageServlet extends HttpServlet {

    private OrderService orderService;
    private SessionService sessionService;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = OrderServiceImpl.getInstance();
        sessionService = SessionServiceImpl.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = sessionService.getCart(req.getSession(true));
        req.setAttribute(ORDER_REQUEST_PARAMETER, orderService.createOrder(cart));
        req.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = sessionService.getCart(req.getSession(true));

        Map<String, String> errors = new HashMap<>();
        OrderDetails orderDetails = extractOrderDetailsFromRequest(req, errors);
        CustomerDetails customerDetails = extractCustomerDetailsFromRequest(req, errors);

        Order order = orderService.createOrder(cart, customerDetails, orderDetails);
        req.setAttribute("order", order);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            resp.sendRedirect(req.getContextPath() + "/order/overview/" + order.getSecureId());

        } else {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(req, resp);
        }
    }

    private OrderDetails extractOrderDetailsFromRequest(HttpServletRequest request, Map<String, String> errors) {
        OrderDetails orderDetails = new OrderDetails();
        setPaymentMethod(request, errors, orderDetails);
        try {
            setRequiredParameter(request, DELIVERY_DATE_REQUEST_PARAMETER, errors, p ->
                    orderDetails.setDeliveryDate(LocalDate.parse(p)));
        } catch (DateTimeParseException e) {
            errors.put(DELIVERY_DATE_REQUEST_PARAMETER, INVALID_DATE_MESSAGE);
        }
        return orderDetails;
    }

    private CustomerDetails extractCustomerDetailsFromRequest(HttpServletRequest request, Map<String, String> errors) {
        CustomerDetails customerDetails = new CustomerDetails();
        setRequiredParameter(request, FIRST_NAME_REQUEST_PARAMETER, errors, customerDetails::setFirstName);
        setRequiredParameter(request, LAST_NAME_REQUEST_PARAMETER, errors, customerDetails::setLastName);
        setRequiredParameter(request, PHONE_NUMBER_REQUEST_PARAMETER, "^\\d{9}$", errors, customerDetails::setPhone);
        setRequiredParameter(request, DELIVERY_ADDRESS_REQUEST_PARAMETER, errors, customerDetails::setDeliveryAddress);
        return customerDetails;
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        setRequiredParameter(request, parameter, ".*", errors, consumer);
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, String regexToMatch, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, REQUIRED_VALUE_MESSAGE);
        } else if (!Pattern.compile(regexToMatch).matcher(value).matches()) {
            errors.put(parameter, INPUT_DOESNT_MATCH_PATTERN_MESSAGE);
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, OrderDetails orderDetails) {
        String value = request.getParameter(PAYMENT_METHOD_REQUEST_PARAMETER);
        if (value == null) {
            errors.put(PAYMENT_METHOD_REQUEST_PARAMETER, PAYMENT_METHOD_NOT_SELECTED_MESSAGE);
        } else {
            orderDetails.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
}
