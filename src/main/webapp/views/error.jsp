<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ошибка</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-danger">
        <h4>Произошла ошибка</h4>
        <p>${error}</p>
    </div>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">На главную</a>
</div>
</body>
</html>
