<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h4 class="card-title mb-0 text-center">
                    <i class="fas fa-user-circle me-2"></i>Об авторе
                </h4>
            </div>
            <div class="card-body">
                <div class="text-center mb-4">
                    <div class="bg-primary rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                         style="width: 100px; height: 100px;">
                        <i class="fas fa-user fa-3x text-white"></i>
                    </div>
                    <h3 class="mb-1">Shop Management System</h3>
                    <p class="text-muted">Веб-приложение для управления магазином</p>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header bg-light">
                                <h6 class="mb-0"><i class="fas fa-info-circle me-2"></i>Информация о проекте</h6>
                            </div>
                            <div class="card-body">
                                <ul class="list-unstyled">
                                    <li class="mb-2">
                                        <strong><i class="fas fa-tag me-2 text-primary"></i>Версия:</strong> 1.0
                                    </li>
                                    <li class="mb-2">
                                        <strong><i class="fas fa-calendar me-2 text-primary"></i>Дата создания:</strong> 2024
                                    </li>
                                    <li class="mb-2">
                                        <strong><i class="fas fa-code me-2 text-primary"></i>Технологии:</strong> Java, JSP, Servlet
                                    </li>
                                    <li>
                                        <strong><i class="fas fa-database me-2 text-primary"></i>База данных:</strong> MySQL
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header bg-light">
                                <h6 class="mb-0"><i class="fas fa-cogs me-2"></i>Функциональность</h6>
                            </div>
                            <div class="card-body">
                                <ul class="list-unstyled">
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>Управление товарами
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>Управление клиентами
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>Управление заказами
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>Статистика и отчеты
                                    </li>
                                    <li>
                                        <i class="fas fa-check text-success me-2"></i>Безопасность и валидация
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header bg-light">
                        <h6 class="mb-0"><i class="fas fa-shield-alt me-2"></i>Функции безопасности</h6>
                    </div>
                    <div class="card-body">
                        <div class="row text-center">
                            <div class="col-md-3 mb-3">
                                <div class="border rounded p-3">
                                    <i class="fas fa-lock fa-2x text-success mb-2"></i>
                                    <h6>CSRF защита</h6>
                                </div>
                            </div>
                            <div class="col-md-3 mb-3">
                                <div class="border rounded p-3">
                                    <i class="fas fa-ban fa-2x text-danger mb-2"></i>
                                    <h6>XSS защита</h6>
                                </div>
                            </div>
                            <div class="col-md-3 mb-3">
                                <div class="border rounded p-3">
                                    <i class="fas fa-code fa-2x text-warning mb-2"></i>
                                    <h6>SQL инъекции</h6>
                                </div>
                            </div>
                            <div class="col-md-3 mb-3">
                                <div class="border rounded p-3">
                                    <i class="fas fa-user-shield fa-2x text-info mb-2"></i>
                                    <h6>Авторизация</h6>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                        <i class="fas fa-arrow-left me-2"></i>Назад в панель управления
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>