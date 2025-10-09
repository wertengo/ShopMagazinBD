<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="d-flex justify-content-between mb-3">
    <div>
        <a href="products?action=statistics" class="btn btn-info me-2">
            <i class="fas fa-chart-bar"></i> Статистика
        </a>
    </div>
    <div>
        <a href="products?action=new" class="btn btn-success">
            <i class="fas fa-plus"></i> Добавить товар
        </a>
    </div>
</div>

<!-- Форма поиска -->
<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title mb-0">Поиск товаров</h5>
    </div>
    <div class="card-body">
        <form action="products" method="post" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-3">
                <label for="searchName" class="form-label">Название</label>
                <input type="text" class="form-control" id="searchName" name="name"
                       value="<c:out value='${searchName}'/>" placeholder="Введите название">
            </div>
            <div class="col-md-2">
                <label for="searchCode" class="form-label">Код</label>
                <input type="text" class="form-control" id="searchCode" name="code"
                       value="<c:out value='${searchCode}'/>" placeholder="Код товара">
            </div>
            <div class="col-md-2">
                <label for="searchAvailable" class="form-label">Доступность</label>
                <select class="form-select" id="searchAvailable" name="available">
                    <option value="">Все</option>
                    <option value="true" <c:if test="${searchAvailable == 'true'}">selected</c:if>>В наличии</option>
                    <option value="false" <c:if test="${searchAvailable == 'false'}">selected</c:if>>Нет в наличии</option>
                </select>
            </div>
            <div class="col-md-2">
                <label for="searchMinPrice" class="form-label">Мин. цена</label>
                <input type="number" class="form-control" id="searchMinPrice" name="minPrice"
                       value="<c:out value='${searchMinPrice}'/>" placeholder="0" step="0.01">
            </div>
            <div class="col-md-2">
                <label for="searchMaxPrice" class="form-label">Макс. цена</label>
                <input type="number" class="form-control" id="searchMaxPrice" name="maxPrice"
                       value="<c:out value='${searchMaxPrice}'/>" placeholder="100000" step="0.01">
            </div>
            <div class="col-md-1 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Таблица товаров -->
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="card-title mb-0">Список товаров</h5>
        <span class="badge bg-secondary">Всего: ${products.size()}</span>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Код</th>
                    <th>Название</th>
                    <th>Цена</th>
                    <th>Количество</th>
                    <th>Доступность</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td>${product.productId}</td>
                        <td>${product.code}</td>
                        <td>
                                ${product.productName}
                            <c:if test="${not empty product.productDescription}">
                                <br><small class="text-muted">${product.productDescription}</small>
                            </c:if>
                        </td>
                        <td><fmt:formatNumber value="${product.productPrice}" pattern="#,##0.00"/> ₽</td>
                        <td>
                                <span class="badge <c:choose>
                                    <c:when test="${product.productQuantity > 10}">bg-success</c:when>
                                    <c:when test="${product.productQuantity > 0}">bg-warning</c:when>
                                    <c:otherwise>bg-danger</c:otherwise>
                                </c:choose>">
                                        ${product.productQuantity}
                                </span>
                        </td>
                        <td>
                                <span class="badge <c:if test="${product.productAvailable}">bg-success</c:if><c:if test="${not product.productAvailable}">bg-danger</c:if>">
                                    <c:choose>
                                        <c:when test="${product.productAvailable}">В наличии</c:when>
                                        <c:otherwise>Нет в наличии</c:otherwise>
                                    </c:choose>
                                </span>
                        </td>
                        <td class="table-actions">
                            <a href="products?action=edit&id=${product.productId}" class="btn btn-sm btn-outline-primary">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="products?action=delete&id=${product.productId}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirmDelete('Удалить товар &quot;${product.productName}&quot;?')">
                                <i class="fas fa-trash"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr>
                        <td colspan="7" class="text-center text-muted py-4">
                            <i class="fas fa-box-open fa-2x mb-2"></i><br>
                            Товары не найдены
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <!-- Пагинация -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                        <a class="page-link" href="products?page=${currentPage - 1}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchCode}">&code=${searchCode}</c:if>">Предыдущая</a>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item <c:if test="${currentPage == i}">active</c:if>">
                            <a class="page-link" href="products?page=${i}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchCode}">&code=${searchCode}</c:if>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                        <a class="page-link" href="products?page=${currentPage + 1}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchCode}">&code=${searchCode}</c:if>">Следующая</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
</div>
