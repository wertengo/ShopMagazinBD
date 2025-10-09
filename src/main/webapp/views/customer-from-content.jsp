<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <c:choose>
                        <c:when test="${customer != null}">Редактирование клиента</c:when>
                        <c:otherwise>Добавление нового клиента</c:otherwise>
                    </c:choose>
                </h5>
            </div>
            <div class="card-body">
                <form action="customers" method="post" onsubmit="return validateForm(this)">
                    <c:if test="${customer != null}">
                        <input type="hidden" name="id" value="<c:out value='${customer.customerId}'/>">
                        <input type="hidden" name="action" value="update">
                    </c:if>
                    <c:if test="${customer == null}">
                        <input type="hidden" name="action" value="insert">
                    </c:if>

                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="firstName" class="form-label">Имя <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="firstName" name="firstName"
                                   value="<c:out value='${customer.customerName}'/>" required
                                   maxlength="50">
                        </div>

                        <div class="col-md-4">
                            <label for="lastName" class="form-label">Фамилия <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="lastName" name="lastName"
                                   value="<c:out value='${customer.customerLastName}'/>" required
                                   maxlength="50">
                        </div>

                        <div class="col-md-4">
                            <label for="surname" class="form-label">Отчество</label>
                            <input type="text" class="form-control" id="surname" name="surname"
                                   value="<c:out value='${customer.customerSurname}'/>"
                                   maxlength="50">
                        </div>

                        <div class="col-md-6">
                            <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<c:out value='${customer.customerEmail}'/>" required
                                   maxlength="100">
                        </div>

                        <div class="col-md-6">
                            <label for="phone" class="form-label">Телефон <span class="text-danger">*</span></label>
                            <input type="tel" class="form-control" id="phone" name="phone"
                                   value="<c:out value='${customer.customerPhoneNumber}'/>" required
                                   maxlength="20" placeholder="+7 (XXX) XXX-XX-XX">
                        </div>

                        <div class="col-12">
                            <label for="address" class="form-label">Адрес <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="address" name="address"
                                      rows="3" required maxlength="255"><c:out value="${customer.customerAddress}"/></textarea>
                        </div>

                        <div class="col-12">
                            <div class="d-flex justify-content-between">
                                <a href="customers" class="btn btn-secondary">Отмена</a>
                                <button type="submit" class="btn btn-success">
                                    <c:choose>
                                        <c:when test="${customer != null}">Обновить клиента</c:when>
                                        <c:otherwise>Добавить клиента</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
