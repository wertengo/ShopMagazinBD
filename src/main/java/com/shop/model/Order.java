package com.shop.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer orderId;
    private Integer customerId;
    private String orderNumber;
    private Date orderDate;
    private BigDecimal orderCost;
    private String orderPaymentMethod;
    private String orderStatus;
    private String orderShippingAddress;
    private Date orderStatusChangedDate;
    private String customerName; // Для отображения в UI

    // Конструкторы, геттеры и сеттеры
    public Order() {}

    // Геттеры и сеттеры...
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public BigDecimal getOrderCost() { return orderCost; }
    public void setOrderCost(BigDecimal orderCost) { this.orderCost = orderCost; }

    public String getOrderPaymentMethod() { return orderPaymentMethod; }
    public void setOrderPaymentMethod(String orderPaymentMethod) { this.orderPaymentMethod = orderPaymentMethod; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getOrderShippingAddress() { return orderShippingAddress; }
    public void setOrderShippingAddress(String orderShippingAddress) { this.orderShippingAddress = orderShippingAddress; }

    public Date getOrderStatusChangedDate() { return orderStatusChangedDate; }
    public void setOrderStatusChangedDate(Date orderStatusChangedDate) { this.orderStatusChangedDate = orderStatusChangedDate; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}