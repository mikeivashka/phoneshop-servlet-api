<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="fieldName" required="true" %>
<%@attribute name="value" required="true" %>
<%@attribute name="title" required="true" %>
<label for="${fieldName}">${title}</label>
<input id="${fieldName}" name="${fieldName}"
       value="${value}" readonly/>
<br>