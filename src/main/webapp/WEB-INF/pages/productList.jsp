<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input type="text" name="query" placeholder="Find products" value="${param.get("query")}"/>
        <button type="submit">Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td><strong>Image</strong></td>
            <td>
                <strong>Description</strong>
                <tags:sortSelect fieldName="description" ascendingLabel="Alphabetic A-Z"
                                 descendingLabel="Alphabetic Z-A"/>
            </td>
            <td class="price">
                <strong>Price</strong>
                <tags:sortSelect fieldName="price"/>
            </td>
            <td><strong>Quantity</strong></td>
            <td></td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${product.imageUrl}"
                         alt="Image not available">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
                </td>
                <td class="price">
                    <tags:priceHistoryAlert product="${product}"/>
                </td>
                <c:set var="inCart" value="0"/>
                <c:forEach var="cartItem" items="${sessionScope.cart.items}">
                    <c:if test="${cartItem.product.equals(product)}">
                        <c:set var="inCart" value="${cartItem.quantity}"/>
                    </c:if>
                </c:forEach>
                <form method="post">
                    <td>
                        <input name="productId" type="hidden" value="${product.id}">
                        <input name="quantity" value="${inCart eq 0 ? 1 : inCart}" type="number" min="0">
                    </td>
                    <td>
                        <button>${inCart eq 0 ? 'Add to cart' : 'Change amount'}</button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </table>
</tags:master>