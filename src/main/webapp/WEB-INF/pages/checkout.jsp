<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order">
    <c:if test="${not empty errors}">
        <div class="error"> Error occurred while placing order</div>
    </c:if>
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
            <td></td>
            <td></td>
            <td class="price">
                Subtotal: ${order.cart.totalPrice}<br>
                Delivery cost: ${order.orderDetails.deliveryCost}<br>
                Total: ${order.totalPrice}
            </td>
            <td>Total quantity: ${order.cart.totalQuantity}</td>
        </tr>
    </table>
    <h2>Details</h2>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <tags:validatedInput errors='${errors}' fieldName='firstName' title='First name'
                             validValue='${order.customerDetails.firstName}'/>
        <tags:validatedInput errors='${errors}' fieldName='lastName' title='Last name'
                             validValue='${order.customerDetails.lastName}'/>
        <tags:validatedInput errors='${errors}' fieldName='phone' title='Phone'
                             validValue='${order.customerDetails.phone}'/>
        <tags:validatedInput errors='${errors}' fieldName='deliveryDate' title='Delivery date'
                             validValue='${order.orderDetails.deliveryDate}'/>
        <tags:validatedInput errors='${errors}' fieldName='deliveryAddress' title='Delivery address'
                             validValue='${order.customerDetails.deliveryAddress}'/>
        <label for="paymentMethod">Payment method<span style="color:red">*</span></label>
        <select id="paymentMethod" name="paymentMethod">
            <option ${order.orderDetails.paymentMethod eq 'CASH' ? 'selected' : ''} value='CASH'>Cash</option>
            <option ${order.orderDetails.paymentMethod eq 'CREDIT_CARD' ? 'selected' : ''} value='CREDIT_CARD'>Credit
                card
            </option>
        </select>
        <br>
        <button>Place order</button>
    </form>
</tags:master>