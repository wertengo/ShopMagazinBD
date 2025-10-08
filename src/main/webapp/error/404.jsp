<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 - Страница не найдена</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <h1 class="display-1">404</h1>
            <h2>Страница не найдена</h2>
            <p class="mt-4">Запрошенная страница не существует.</p>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary mt-3">Вернуться на главную</a>
        </div>
    </div>
</div>
</body>
</html>