package com.shop.dao;

import com.shop.model.Customer;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers(int offset, int limit) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer ORDER BY customer_id LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    public List<Customer> searchCustomers(String name, String email, String phone, int offset, int limit) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Customer WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND (customer_name LIKE ? OR customer_lastName LIKE ?)");
            params.add("%" + StringEscapeUtils.escapeJava(name) + "%");
            params.add("%" + StringEscapeUtils.escapeJava(name) + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            sql.append(" AND customer_email LIKE ?");
            params.add("%" + StringEscapeUtils.escapeJava(email) + "%");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND customer_phoneNumber LIKE ?");
            params.add("%" + StringEscapeUtils.escapeJava(phone) + "%");
        }

        sql.append(" ORDER BY customer_id LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    // ДОБАВЛЕННЫЙ МЕТОД: Получить клиента по ID
    public Customer getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        }
        return null;
    }

    public boolean addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (customer_name, customer_lastName, customer_surname, " +
                "customer_address, customer_phoneNumber, customer_email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StringEscapeUtils.escapeJava(customer.getCustomerName()));
            stmt.setString(2, StringEscapeUtils.escapeJava(customer.getCustomerLastName()));
            stmt.setString(3, StringEscapeUtils.escapeJava(customer.getCustomerSurname()));
            stmt.setString(4, StringEscapeUtils.escapeJava(customer.getCustomerAddress()));
            stmt.setString(5, StringEscapeUtils.escapeJava(customer.getCustomerPhoneNumber()));
            stmt.setString(6, StringEscapeUtils.escapeJava(customer.getCustomerEmail()));

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET customer_name = ?, customer_lastName = ?, customer_surname = ?, " +
                "customer_address = ?, customer_phoneNumber = ?, customer_email = ? " +
                "WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StringEscapeUtils.escapeJava(customer.getCustomerName()));
            stmt.setString(2, StringEscapeUtils.escapeJava(customer.getCustomerLastName()));
            stmt.setString(3, StringEscapeUtils.escapeJava(customer.getCustomerSurname()));
            stmt.setString(4, StringEscapeUtils.escapeJava(customer.getCustomerAddress()));
            stmt.setString(5, StringEscapeUtils.escapeJava(customer.getCustomerPhoneNumber()));
            stmt.setString(6, StringEscapeUtils.escapeJava(customer.getCustomerEmail()));
            stmt.setInt(7, customer.getCustomerId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public int getTotalCustomersCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Customer";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // ДОБАВЛЕННЫЙ МЕТОД: Преобразование ResultSet в объект Customer
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("customer_id"),
                rs.getString("customer_name"),
                rs.getString("customer_lastName"),
                rs.getString("customer_surname"),
                rs.getString("customer_address"),
                rs.getString("customer_phoneNumber"),
                rs.getString("customer_email")
        );
    }
}