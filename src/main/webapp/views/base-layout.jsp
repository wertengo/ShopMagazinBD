<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Shop Management'}"/></title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
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
        .user-info {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 0.375rem;
        }
        .logout-btn {
            transition: all 0.3s ease;
        }
        .logout-btn:hover {
            transform: translateX(-3px);
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
            <div class="position-sticky pt-3">
                <!-- Информация о пользователе -->
                <div class="user-info text-center">
                    <i class="fas fa-user-circle fa-2x mb-2"></i>
                    <h6 class="mb-1">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <c:out value="${sessionScope.user}"/>
                            </c:when>
                            <c:otherwise>
                                Администратор
                            </c:otherwise>
                        </c:choose>
                    </h6>
                    <small>Система управления</small>
                </div>

                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted text-uppercase">
                    <span>Управление</span>
                </h6>
                <ul class="nav flex-column mb-2">
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

                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted text-uppercase">
                    <span>Система</span>
                </h6>
                <ul class="nav flex-column mb-2">
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${activePage == 'author'}">active</c:if>" href="${pageContext.request.contextPath}/author">
                            <i class="fas fa-user-circle"></i>
                            Об авторе
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link logout-btn text-danger" href="${pageContext.request.contextPath}/login?action=logout">
                            <i class="fas fa-sign-out-alt"></i>
                            Выйти
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Main content -->
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2"><c:out value="${pageTitle != null ? pageTitle : 'Панель управления'}"/></h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <div class="btn-group me-2">
                        <span class="btn btn-outline-secondary">
                            <i class="fas fa-user me-1"></i>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <c:out value="${sessionScope.user}"/>
                                </c:when>
                                <c:otherwise>
                                    Администратор
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <a href="${pageContext.request.contextPath}/author" class="btn btn-outline-primary">
                            <i class="fas fa-info-circle me-1"></i> О проекте
                        </a>
                        <a href="${pageContext.request.contextPath}/login?action=logout" class="btn btn-outline-danger">
                            <i class="fas fa-sign-out-alt me-1"></i> Выйти
                        </a>
                    </div>
                </div>
            </div>

            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    <c:out value="${message}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <c:out value="${error}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Dynamic content -->
            <jsp:include page="${contentPage}"/>
        </main>
    </div>
</div>

<!-- Bootstrap JS -->
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

    // Автоматическое скрытие alert через 5 секунд
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            setTimeout(function() {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, 5000);
        });
    });
</script>
</body>
</html>