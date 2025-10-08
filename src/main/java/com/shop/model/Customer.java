package com.shop.model;

public class Customer {
    private Integer customerId;
    private String customerName;
    private String customerLastName;
    private String customerSurname;
    private String customerAddress;
    private String customerPhoneNumber;
    private String customerEmail;

    // Конструкторы, геттеры и сеттеры
    public Customer() {}

    public Customer(Integer customerId, String customerName, String customerLastName,
                    String customerSurname, String customerAddress,
                    String customerPhoneNumber, String customerEmail) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerLastName = customerLastName;
        this.customerSurname = customerSurname;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerEmail = customerEmail;
    }

    // Геттеры и сеттеры...
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    public String getCustomerSurname() { return customerSurname; }
    public void setCustomerSurname(String customerSurname) { this.customerSurname = customerSurname; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getCustomerPhoneNumber() { return customerPhoneNumber; }
    public void setCustomerPhoneNumber(String customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}