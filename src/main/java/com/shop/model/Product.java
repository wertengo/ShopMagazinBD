package com.shop.model;

import java.math.BigDecimal;

public class Product {
    private Integer productId;
    private String code;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal productLength;
    private Integer productQuantity;
    private Boolean productAvailable;
    private String productDescription;

    // Конструкторы, геттеры и сеттеры
    public Product() {}

    public Product(Integer productId, String code, String productName,
                   BigDecimal productPrice, BigDecimal productLength,
                   Integer productQuantity, Boolean productAvailable,
                   String productDescription) {
        this.productId = productId;
        this.code = code;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productLength = productLength;
        this.productQuantity = productQuantity;
        this.productAvailable = productAvailable;
        this.productDescription = productDescription;
    }

    // Геттеры и сеттеры...
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public BigDecimal getProductLength() { return productLength; }
    public void setProductLength(BigDecimal productLength) { this.productLength = productLength; }

    public Integer getProductQuantity() { return productQuantity; }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity = productQuantity; }

    public Boolean getProductAvailable() { return productAvailable; }
    public void setProductAvailable(Boolean productAvailable) { this.productAvailable = productAvailable; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
}