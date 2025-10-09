<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Shop Management'}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .navbar-brand {
            font-weight: bold;
        }
        .stat-card {
            transition: transform 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .table-actions {
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
            <div class="position-sticky pt-3">
                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted text-uppercase">
                    <span>Управление</span>
                </h6>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${activePage == 'dashboard'}">active</c:if>" href="${pageContext.request.contextPath}/dashboard">
                            <i class="fas fa-tachometer-alt"></i>
                            Панель управления
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${activePage == 'products'}">active</c:if>" href="${pageContext.request.contextPath}/products">
                            <i class="fas fa-box"></i>
                            Товары
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${activePage == 'customers'}">active</c:if>" href="${pageContext.request.contextPath}/customers">
                            <i class="fas fa-users"></i>
                            Клиенты
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${activePage == 'orders'}">active</c:if>" href="${pageContext.request.contextPath}/orders">
                            <i class="fas fa-shopping-cart"></i>
                            Заказы
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Main content -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><c:out value="${pageTitle != null ? pageTitle : 'Панель управления'}"/></h1>
            </div>

            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <c:out value="${message}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${error}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Dynamic content -->
            <jsp:include page="${contentPage}"/>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function confirmDelete(message) {
        return confirm(message || 'Вы уверены, что хотите удалить эту запись?');
    }

    function validateForm(form) {
        const requiredFields = form.querySelectorAll('[required]');
        for (let field of requiredFields) {
            if (!field.value.trim()) {
                alert('Пожалуйста, заполните все обязательные поля');
                field.focus();
                return false;
            }
        }
        return true;
    }
</script>
</body>
</html>
