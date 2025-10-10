<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <c:choose>
                        <c:when test="${product != null}">Редактирование товара</c:when>
                        <c:otherwise>Добавление нового товара</c:otherwise>
                    </c:choose>
                </h5>
            </div>
            <div class="card-body">
                <form action="products" method="post" onsubmit="return validateProductForm(this)">
                    <c:if test="${product != null}">
                        <input type="hidden" name="id" value="<c:out value='${product.productId}'/>">
                        <input type="hidden" name="action" value="update">
                    </c:if>
                    <c:if test="${product == null}">
                        <input type="hidden" name="action" value="insert">
                    </c:if>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="code" class="form-label">Код товара <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="code" name="code"
                                   value="<c:out value='${product.code}'/>" required
                                   pattern="[A-Za-z0-9-]+" title="Только буквы, цифры и дефисы"
                                   maxlength="20">
                            <div class="form-text">Уникальный код товара</div>
                        </div>

                        <div class="col-md-6">
                            <label for="name" class="form-label">Название товара <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="<c:out value='${product.productName}'/>" required
                                   maxlength="100">
                        </div>

                        <div class="col-md-4">
                            <label for="price" class="form-label">Цена <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <input type="number" class="form-control" id="price" name="price"
                                       value="<c:out value='${product.productPrice}'/>"
                                       step="0.01" min="0" required>
                                <span class="input-group-text">₽</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label for="length" class="form-label">Длина (см)</label>
                            <div class="input-group">
                                <input type="number" class="form-control" id="length" name="length"
                                       value="<c:out value='${product.productLength}'/>"
                                       step="0.1" min="0">
                                <span class="input-group-text">см</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label for="quantity" class="form-label">Количество <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" id="quantity" name="quantity"
                                   value="<c:out value='${product.productQuantity != null ? product.productQuantity : 0}'/>"
                                   min="0" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Доступность</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="available"
                                       id="availableTrue" value="true"
                                       <c:if test="${product == null || product.productAvailable}">checked</c:if>>
                                <label class="form-check-label" for="availableTrue">
                                    В наличии
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="available"
                                       id="availableFalse" value="false"
                                       <c:if test="${product != null && !product.productAvailable}">checked</c:if>>
                                <label class="form-check-label" for="availableFalse">
                                    Нет в наличии
                                </label>
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="description" class="form-label">Описание</label>
                            <textarea class="form-control" id="description" name="description"
                                      rows="4" maxlength="500"><c:out value="${product.productDescription}"/></textarea>
                            <div class="form-text">Максимум 500 символов</div>
                        </div>

                        <div class="col-12">
                            <div class="d-flex justify-content-between">
                                <a href="products" class="btn btn-secondary">Отмена</a>
                                <button type="submit" class="btn btn-success">
                                    <c:choose>
                                        <c:when test="${product != null}">Обновить товар</c:when>
                                        <c:otherwise>Добавить товар</c:otherwise>
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

<script>
    function validateProductForm(form) {
        const price = form.querySelector('#price');
        const quantity = form.querySelector('#quantity');

        if (parseFloat(price.value) < 0) {
            alert('Цена не может быть отрицательной');
            price.focus();
            return false;
        }

        if (parseInt(quantity.value) < 0) {
            alert('Количество не может быть отрицательным');
            quantity.focus();
            return false;
        }

        return validateForm(form);
    }
</script>