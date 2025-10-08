<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>500 - Ошибка сервера</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <h1 class="display-1">500</h1>
            <h2>Внутренняя ошибка сервера</h2>
            <p class="mt-4">Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.</p>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary mt-3">Вернуться на главную</a>
        </div>
    </div>
</div>
</body>
</html>