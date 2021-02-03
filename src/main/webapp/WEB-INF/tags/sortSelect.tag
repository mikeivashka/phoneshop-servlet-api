<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="fieldName" required="true" %>
<%@attribute name="ascendingLabel" required="false" %>
<%@attribute name="descendingLabel" required="false" %>
<%@attribute name="selected" required="false" %>
<c:set var="ascendingLabel" value="${(empty ascendingLabel) ? 'Ascending' : ascendingLabel}"/>
<c:set var="descendingLabel" value="${(empty descendingLabel) ? 'Descending' : descendingLabel}"/>
<c:set var="selected"
       value="${param.get('sort') eq fieldName ? param.get('order') : selected }"/>
<form>
    <label for="${fieldName}Sort">Sorting:</label>
    <input type="hidden" name="sort" value="${fieldName}">
    <input type="hidden" name="query" value="${param.get('query')}">
    <select id="${fieldName}Sort" name="order"
            onchange='this.form.submit()'>
        <option value='none'>Default</option>
        <option ${selected eq 'ascending' ? 'selected' : ''} value='ascending'>${ascendingLabel}</option>
        <option ${selected eq 'descending' ? 'selected' : ''} value='descending'>${descendingLabel}</option>
    </select>
</form>