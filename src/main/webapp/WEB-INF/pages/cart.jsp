<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <c:if test="${not empty errors}">
        <div class="error">
            There were errors during cart update
        </div>
    </c:if>
    <form id="cartInfo" method="post" action="${pageContext.servletContext.contextPath}/cart">
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
            <c:forEach var="item" items="${cart.items}" varStatus="status">
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
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input name="productId" type="hidden" value="${item.product.id}">
                        <input id="${item.product.id}" name="quantity" type="text" min="0" max="${item.product.stock}"
                               value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}">
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${errors[item.product.id]}
                            </div>
                        </c:if>
                    </td>
                    <td>
                        <button onclick="deleteCartItem(${item.product.id})">Delete</button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p>
            <button>Update</button>
        </p>
    </form>
</tags:master>
<script type="text/javascript">
    function deleteCartItem(id) {
        document.getElementById(id).value = 0;
        document.getElementById('cartInfo').submit();
    }
</script>