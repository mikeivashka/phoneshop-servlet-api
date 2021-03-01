<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="errors" required="true" type="java.util.Map" %>
<%@attribute name="fieldName" required="true" %>
<%@attribute name="validValue" required="true" %>
<%@attribute name="title" required="true" %>
<c:set var="error" value="${errors[fieldName]}"/>
<label for="${fieldName}">${title}<span style="color:red">*</span></label>
<input id="${fieldName}" name="${fieldName}"
       value="${not empty error ? param[fieldName] : validValue}"/>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<br>