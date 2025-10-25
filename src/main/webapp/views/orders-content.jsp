<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Сообщения об успехе/ошибке -->
<c:if test="${not empty message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${message == 'Order status updated successfully'}">Статус заказа успешно обновлен</c:when>
            <c:otherwise>${message}</c:otherwise>
        </c:choose>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="fas fa-exclamation-triangle"></i> ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- Форма поиска -->
<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title mb-0">Поиск заказов</h5>
    </div>
    <div class="card-body">
        <form action="orders" method="post" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-3">
                <label for="searchOrderNumber" class="form-label">Номер заказа</label>
                <input type="text" class="form-control" id="searchOrderNumber" name="orderNumber"
                       value="<c:out value='${searchOrderNumber}'/>" placeholder="Введите номер заказа"
                       maxlength="50" pattern="[A-Za-z0-9-]*" title="Только буквы, цифры и дефисы">
            </div>
            <div class="col-md-3">
                <label for="searchStatus" class="form-label">Статус</label>
                <select class="form-select" id="searchStatus" name="status">
                    <option value="">Все</option>
                    <option value="Pending" <c:if test="${searchStatus == 'Pending'}">selected</c:if>>Ожидание</option>
                    <option value="Processing" <c:if test="${searchStatus == 'Processing'}">selected</c:if>>Обработка</option>
                    <option value="Shipped" <c:if test="${searchStatus == 'Shipped'}">selected</c:if>>Отправлен</option>
                    <option value="Delivered" <c:if test="${searchStatus == 'Delivered'}">selected</c:if>>Доставлен</option>
                    <option value="Cancelled" <c:if test="${searchStatus == 'Cancelled'}">selected</c:if>>Отменен</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="searchCustomerName" class="form-label">Клиент</label>
                <input type="text" class="form-control" id="searchCustomerName" name="customerName"
                       value="<c:out value='${searchCustomerName}'/>" placeholder="Введите имя клиента"
                       maxlength="100">
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Таблица заказов -->
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="card-title mb-0">Список заказов</h5>
        <span class="badge bg-secondary">Всего: ${orders.size()}</span>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Номер заказа</th>
                    <th>Клиент</th>
                    <th>Дата</th>
                    <th>Стоимость</th>
                    <th>Статус</th>
                    <th>Способ оплаты</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderId}</td>
                        <td><c:out value="${order.orderNumber}"/></td>
                        <td><c:out value="${order.customerName}"/></td>
                        <td><fmt:formatDate value="${order.orderDate}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td><fmt:formatNumber value="${order.orderCost}" pattern="#,##0.00"/> ₽</td>
                        <td>
                                <span class="badge
                                    <c:choose>
                                        <c:when test="${order.orderStatus == 'Delivered'}">bg-success</c:when>
                                        <c:when test="${order.orderStatus == 'Shipped'}">bg-info</c:when>
                                        <c:when test="${order.orderStatus == 'Processing'}">bg-primary</c:when>
                                        <c:when test="${order.orderStatus == 'Pending'}">bg-warning</c:when>
                                        <c:when test="${order.orderStatus == 'Cancelled'}">bg-danger</c:when>
                                        <c:otherwise>bg-secondary</c:otherwise>
                                    </c:choose>">
                                        <c:out value="${order.orderStatus}"/>
                                </span>
                        </td>
                        <td><c:out value="${order.orderPaymentMethod}"/></td>
                        <td class="table-actions">
                            <div class="dropdown">
                                <button class="btn btn-sm btn-outline-secondary dropdown-toggle"
                                        type="button" data-bs-toggle="dropdown"
                                        aria-expanded="false">
                                    <i class="fas fa-cog"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a class="dropdown-item" href="#"
                                           onclick="updateOrderStatus(${order.orderId}, 'Processing')">
                                            <i class="fas fa-cog text-primary"></i> В обработку
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="#"
                                           onclick="updateOrderStatus(${order.orderId}, 'Shipped')">
                                            <i class="fas fa-shipping-fast text-info"></i> Отправить
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="#"
                                           onclick="updateOrderStatus(${order.orderId}, 'Delivered')">
                                            <i class="fas fa-check text-success"></i> Доставлен
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item text-danger" href="#"
                                           onclick="updateOrderStatus(${order.orderId}, 'Cancelled')">
                                            <i class="fas fa-times"></i> Отменить
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty orders}">
                    <tr>
                        <td colspan="8" class="text-center text-muted py-4">
                            <i class="fas fa-shopping-cart fa-2x mb-2"></i><br>
                            Заказы не найдены
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
                        <a class="page-link" href="orders?page=${currentPage - 1}<c:if test="${not empty searchOrderNumber}">&orderNumber=<c:out value="${searchOrderNumber}"/></c:if><c:if test="${not empty searchStatus}">&status=<c:out value="${searchStatus}"/></c:if><c:if test="${not empty searchCustomerName}">&customerName=<c:out value="${searchCustomerName}"/></c:if>">
                            Предыдущая
                        </a>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item <c:if test="${currentPage == i}">active</c:if>">
                            <a class="page-link" href="orders?page=${i}<c:if test="${not empty searchOrderNumber}">&orderNumber=<c:out value="${searchOrderNumber}"/></c:if><c:if test="${not empty searchStatus}">&status=<c:out value="${searchStatus}"/></c:if><c:if test="${not empty searchCustomerName}">&customerName=<c:out value="${searchCustomerName}"/></c:if>">
                                    ${i}
                            </a>
                        </li>
                    </c:forEach>

                    <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                        <a class="page-link" href="orders?page=${currentPage + 1}<c:if test="${not empty searchOrderNumber}">&orderNumber=<c:out value="${searchOrderNumber}"/></c:if><c:if test="${not empty searchStatus}">&status=<c:out value="${searchStatus}"/></c:if><c:if test="${not empty searchCustomerName}">&customerName=<c:out value="${searchCustomerName}"/></c:if>">
                            Следующая
                        </a>
                    </li>
                </ul>
            </nav>

            <!-- Информация о текущей позиции -->
            <div class="text-center text-muted mt-2">
                Страница ${currentPage} из ${totalPages} • Показано ${orders.size()} заказов из ${totalRecords}
            </div>
        </c:if>
    </div>
</div>

<script>
    function updateOrderStatus(orderId, status) {
        const statusNames = {
            'Pending': 'Ожидание',
            'Processing': 'Обработка',
            'Shipped': 'Отправлен',
            'Delivered': 'Доставлен',
            'Cancelled': 'Отменен'
        };

        if (confirm('Изменить статус заказа на \"' + statusNames[status] + '\"?')) {
            fetch('orders?action=updateStatus&id=' + orderId + '&status=' + encodeURIComponent(status), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Ошибка при обновлении статуса');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('Ошибка при обновлении статуса');
            });
        }
    }
</script>
