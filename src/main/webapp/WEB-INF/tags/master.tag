<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<c:set var="recentProducts" value="${sessionScope.get('recentProducts')}"/>
<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
<header>
    <a href="${pageContext.servletContext.contextPath}">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg" alt="logo"/>
        PhoneShop
    </a>
    <a href="${pageContext.servletContext.contextPath}/cart">
        <jsp:include page="/cart/minicart"/>
    </a>
</header>
<main>
    <c:if test="${not empty param.successMessage}">
        <div>
            <b class="success">${param.successMessage}</b>
        </div>
    </c:if>
    <c:if test="${not empty param.errorMessage}">
        <div>
            <b class="error">${param.errorMessage}</b>
        </div>
    </c:if>
    <jsp:doBody/>
    <c:if test="${not empty recentProducts}">
        <div>
            <h2 style="text-align: center">Recently viewed</h2>
            <c:forEach var="product" items="${recentProducts}">
                <div class="container">
                    <img class="recent" src="${product.imageUrl}" alt="No image present"><br>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>
                    <p><fmt:formatNumber value='${product.price}' type="currency"
                                         currencySymbol="${product.currency.symbol}"/></p>
                </div>
            </c:forEach>
        </div>
    </c:if>
</main>
</body>
</html>