<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <th><strong>Image</strong></th>
            <th>
                <strong>Description</strong>
            </th>
            <th class="price">
                <strong>Price</strong>
            </th>
            <th>
                <strong>Quantity</strong>
            </th>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.cart.items}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${item.product.imageUrl}"
                         alt="Image not available">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">${item.product.description}</a>
                </td>
                <td class="price">
                    <fmt:formatNumber value='${item.product.price}' type="currency"
                                      currencySymbol="${item.product.currency.symbol}"/>
                </td>
                <td>
                        ${item.quantity}
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td/>
            <td/>
            <td class="price">
                Subtotal: ${order.cart.totalPrice}<br>
                Delivery cost: ${order.orderDetails.deliveryCost}<br>
                Total: ${order.totalPrice}
            </td>
            <td>Total quantity: ${order.cart.totalQuantity}</td>
        </tr>
    </table>
    <h2>Details</h2>
    <tags:readOnlyLabelledInput fieldName="firstName" value="${order.customerDetails.firstName}"
                                title="First name"/>
    <tags:readOnlyLabelledInput fieldName="lastName" value="${order.customerDetails.lastName}"
                                title="Last name"/>
    <tags:readOnlyLabelledInput fieldName="phone" value="${order.customerDetails.phone}"
                                title="Phone"/>
    <tags:readOnlyLabelledInput fieldName="deliveryDate" value="${order.orderDetails.deliveryDate}"
                                title="Delivery date"/>
    <tags:readOnlyLabelledInput fieldName="deliveryAddress" value="${order.customerDetails.deliveryAddress}"
                                title="Delivery address"/>
    <label for="paymentMethod">Payment method</label>
    <select id="paymentMethod" name="paymentMethod">
        <option ${order.orderDetails.paymentMethod eq 'CASH' ? 'selected' : ''} value='CASH'>Cash</option>
        <option ${order.orderDetails.paymentMethod eq 'CREDIT_CARD' ? 'selected' : ''} value='CREDIT_CARD'>Credit card
        </option>
    </select>
</tags:master>