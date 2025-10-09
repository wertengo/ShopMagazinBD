<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
    <div class="col-md-3 mb-4">
        <div class="card stat-card bg-primary text-white">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h4 class="card-title">${productStats.totalProducts}</h4>
                        <p class="card-text">Товаров</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-box fa-2x"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card stat-card bg-success text-white">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h4 class="card-title">${customerStats.totalCustomers}</h4>
                        <p class="card-text">Клиентов</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-users fa-2x"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card stat-card bg-warning text-dark">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h4 class="card-title">${orderStats.totalOrders}</h4>
                        <p class="card-text">Заказы</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-shopping-cart fa-2x"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card stat-card bg-info text-white">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h4 class="card-title">
                            <c:if test="${not empty orderStats.totalRevenue}">
                                <fmt:formatNumber value="${orderStats.totalRevenue}" pattern="#,##0.00"/> ₽
                            </c:if>
                            <c:if test="${empty orderStats.totalRevenue}">0 ₽</c:if>
                        </h4>
                        <p class="card-text">Общий доход</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-chart-line fa-2x"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Статистика товаров</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Доступно товаров
                        <span class="badge bg-success rounded-pill">${productStats.availableProducts}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Общее количество на складе
                        <span class="badge bg-primary rounded-pill">${productStats.totalQuantity}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Средняя цена
                        <span class="badge bg-info rounded-pill">
                            <c:if test="${not empty productStats.averagePrice}">
                                <fmt:formatNumber value="${productStats.averagePrice}" pattern="#,##0.00"/> ₽
                            </c:if>
                            <c:if test="${empty productStats.averagePrice}">0 ₽</c:if>
                        </span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Статистика заказов</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Доставлено заказов
                        <span class="badge bg-success rounded-pill">${orderStats.deliveredOrders}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Ожидающие заказы
                        <span class="badge bg-warning rounded-pill">${orderStats.pendingOrders}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Средний чек
                        <span class="badge bg-info rounded-pill">
                            <c:if test="${not empty orderStats.averageOrderValue}">
                                <fmt:formatNumber value="${orderStats.averageOrderValue}" pattern="#,##0.00"/> ₽
                            </c:if>
                            <c:if test="${empty orderStats.averageOrderValue}">0 ₽</c:if>
                        </span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>