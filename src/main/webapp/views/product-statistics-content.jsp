<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="d-flex justify-content-between mb-3">
    <div>
        <a href="products" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Назад к списку
        </a>
    </div>
</div>

<div class="row">
    <div class="col-md-3 mb-4">
        <div class="card stat-card bg-primary text-white">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h4 class="card-title">${stats.totalProducts}</h4>
                        <p class="card-text">Всего товаров</p>
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
                        <h4 class="card-title">${stats.availableProducts}</h4>
                        <p class="card-text">Доступно товаров</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-check-circle fa-2x"></i>
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
                        <h4 class="card-title">${stats.totalQuantity}</h4>
                        <p class="card-text">Общее количество</p>
                    </div>
                    <div class="align-self-center">
                        <i class="fas fa-cubes fa-2x"></i>
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
                        <h4 class="card-title">
                            <fmt:formatNumber value="${stats.averagePrice}" pattern="#,##0.00"/> ₽
                        </h4>
                        <p class="card-text">Средняя цена</p>
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
                <h5 class="card-title mb-0">Ценовая статистика</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Максимальная цена
                        <span class="badge bg-primary rounded-pill">
                            <fmt:formatNumber value="${stats.maxPrice}" pattern="#,##0.00"/> ₽
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Минимальная цена
                        <span class="badge bg-success rounded-pill">
                            <fmt:formatNumber value="${stats.minPrice}" pattern="#,##0.00"/> ₽
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Средняя цена
                        <span class="badge bg-info rounded-pill">
                            <fmt:formatNumber value="${stats.averagePrice}" pattern="#,##0.00"/> ₽
                        </span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Статистика наличия</h5>
            </div>
            <div class="card-body">
                <ul class="list-group list-group-flush">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Всего товаров
                        <span class="badge bg-secondary rounded-pill">${stats.totalProducts}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Доступно для продажи
                        <span class="badge bg-success rounded-pill">${stats.availableProducts}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Недоступно
                        <span class="badge bg-danger rounded-pill">${stats.totalProducts - stats.availableProducts}</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>