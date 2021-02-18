<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<c:set var="inCart" value="0"/>
<c:forEach var="cartItem" items="${sessionScope.cart.items}">
    <c:if test="${cartItem.product.equals(product)}">
        <c:set var="inCart" value="${cartItem.quantity}"/>
    </c:if>
</c:forEach>
<c:set var="totalPrice" value="${product.price.doubleValue() * inCart}"/>
<tags:master pageTitle="${product.description}">
    <table>
        <tbody>
        <tr>
            <td>Image</td>
            <td>
                <img src="${product.imageUrl}" alt="No image found :(">
            </td>
        </tr>
        <tr>
            <td>Code</td>
            <td>${product.code}</td>
        </tr>
        <tr>
            <td>Stock</td>
            <td>${product.stock}</td>
        </tr>
        <tr>
            <td>Description</td>
            <td>${product.description}</td>
        </tr>
        <tr>
            <td>Price</td>
            <td>
                <tags:priceHistoryAlert product="${product}"/>
            </td>
        </tr>
        </tbody>
    </table>
    <form method="post">
        <label for="quantity">${inCart eq 0 ? "Choose amount" : 'Total: '.concat(totalPrice)}<br></label>
        <input
                id="quantity"
                value="${inCart eq 0 ? 1 : inCart}"
                type="number"
                min="0" max="${product.stock}" name="quantity"/>
        <button ${product.stock eq 0 ? 'disabled' : ''}
                type="submit">${inCart eq 0 ? 'Add to cart' : 'Change amount'}</button>
    </form>
</tags:master>
