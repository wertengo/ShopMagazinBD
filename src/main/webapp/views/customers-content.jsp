<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-between mb-3">
    <div>
        <a href="customers?action=new" class="btn btn-success">
            <i class="fas fa-plus"></i> Добавить клиента
        </a>
    </div>
</div>

<!-- Форма поиска -->
<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title mb-0">Поиск клиентов</h5>
    </div>
    <div class="card-body">
        <form action="customers" method="post" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-4">
                <label for="searchName" class="form-label">Имя или фамилия</label>
                <input type="text" class="form-control" id="searchName" name="name"
                       value="<c:out value='${searchName}'/>" placeholder="Введите имя или фамилию">
            </div>
            <div class="col-md-4">
                <label for="searchEmail" class="form-label">Email</label>
                <input type="text" class="form-control" id="searchEmail" name="email"
                       value="<c:out value='${searchEmail}'/>" placeholder="Введите email">
            </div>
            <div class="col-md-3">
                <label for="searchPhone" class="form-label">Телефон</label>
                <input type="text" class="form-control" id="searchPhone" name="phone"
                       value="<c:out value='${searchPhone}'/>" placeholder="Введите телефон">
            </div>
            <div class="col-md-1 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Таблица клиентов -->
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="card-title mb-0">Список клиентов</h5>
        <span class="badge bg-secondary">Всего: ${customers.size()}</span>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Отчество</th>
                    <th>Email</th>
                    <th>Телефон</th>
                    <th>Адрес</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="customer" items="${customers}">
                    <tr>
                        <td>${customer.customerId}</td>
                        <td>${customer.customerName}</td>
                        <td>${customer.customerLastName}</td>
                        <td>${customer.customerSurname}</td>
                        <td>${customer.customerEmail}</td>
                        <td>${customer.customerPhoneNumber}</td>
                        <td>${customer.customerAddress}</td>
                        <td class="table-actions">
                            <a href="customers?action=edit&id=${customer.customerId}" class="btn btn-sm btn-outline-primary">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="customers?action=delete&id=${customer.customerId}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirmDelete('Удалить клиента &quot;${customer.customerName} ${customer.customerLastName}&quot;?')">
                                <i class="fas fa-trash"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty customers}">
                    <tr>
                        <td colspan="8" class="text-center text-muted py-4">
                            <i class="fas fa-users fa-2x mb-2"></i><br>
                            Клиенты не найдены
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
                        <a class="page-link" href="customers?page=${currentPage - 1}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchEmail}">&email=${searchEmail}</c:if>">Предыдущая</a>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item <c:if test="${currentPage == i}">active</c:if>">
                            <a class="page-link" href="customers?page=${i}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchEmail}">&email=${searchEmail}</c:if>">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                        <a class="page-link" href="customers?page=${currentPage + 1}<c:if test="${not empty searchName}">&name=${searchName}</c:if><c:if test="${not empty searchEmail}">&email=${searchEmail}</c:if>">Следующая</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
</div>
