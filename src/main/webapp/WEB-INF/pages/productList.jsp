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
            </tr>
        </c:forEach>
    </table>
</tags:master>