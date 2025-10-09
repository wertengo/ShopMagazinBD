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
                <p>Форма для добавления/редактирования товара</p>
                <a href="products" class="btn btn-secondary">Назад к списку</a>
            </div>
        </div>
    </div>
</div>
