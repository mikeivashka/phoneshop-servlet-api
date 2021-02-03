<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Product not found">
    <h1>We are sorry, but product with
        id ${requestScope.get("javax.servlet.forward.path_info").substring(1)} does not exist or removed</h1>
</tags:master>
