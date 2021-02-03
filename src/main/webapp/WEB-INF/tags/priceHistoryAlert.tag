<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="product" required="true" type="com.es.phoneshop.model.product.Product" %>
<c:set var="priceHistory" value=''/>
<c:forEach var="priceEvent"
           items="${product.priceHistory}">
    <c:set var="priceHistory"
           value='Since ${priceEvent.getTime()}: ${priceEvent.getNewPrice()} ${product.currency.symbol}\n${priceHistory}'/>
</c:forEach>
<button onclick="alert('${priceHistory}'); ">
    <fmt:formatNumber value='${product.price}' type="currency"
                      currencySymbol="${product.currency.symbol}"/>
</button>