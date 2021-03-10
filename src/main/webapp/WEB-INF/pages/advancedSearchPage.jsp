<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.List" scope="request"/>
<tags:master pageTitle="Product Search">
    <h2>Advanced Search<br/></h2>
    <form>
        <label for="description" >Description</label>
        <input name="description" value="${param['description']}" type="text" id="description"/>
        <select name="searchMode">
            <option ${param['searchMode'] eq 'all' ? 'selected' : ''} value="all">All words</option>
            <option ${param['searchMode'] eq 'any' ? 'selected' : ''} value="any">Any of words</option>
        </select>
        <br/>
        <tags:validatedInput errors="${errors}" fieldName="minPrice" validValue="${param['minPrice']}" title="Min price"/>
        <tags:validatedInput errors="${errors}" fieldName="maxPrice" validValue="${param['maxPrice']}" title="Max price"/>
        <button type="submit">Search</button>
    </form>

    <c:if test="${not empty products}">
        <table>
            <thead>
            <tr>
                <td><strong>Image</strong></td>
                <td>
                    <strong>Description</strong>
                </td>
                <td class="price">
                    <strong>Price</strong>
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
    </c:if>

</tags:master>