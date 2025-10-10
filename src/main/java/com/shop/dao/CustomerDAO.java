package com.shop.dao;

import com.shop.model.Customer;

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
            params.add("%" + name + "%");
            params.add("%" + name + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            sql.append(" AND customer_email LIKE ?");
            params.add("%" + email + "%");
        }
        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND customer_phoneNumber LIKE ?");
            params.add("%" + phone + "%");
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

    // Получить клиента по ID
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

    // Добавить клиента
    // Добавить клиента с проверкой на дубликаты
    public boolean addCustomer(Customer customer) throws SQLException {
        // Сначала проверяем, нет ли клиента с таким email
        String checkSql = "SELECT COUNT(*) FROM Customer WHERE customer_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, customer.getCustomerEmail());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Клиент с email " + customer.getCustomerEmail() + " уже существует");
            }
        }

        // Если дубликатов нет, добавляем клиента
        String sql = "INSERT INTO Customer (customer_name, customer_lastName, customer_surname, " +
                "customer_address, customer_phoneNumber, customer_email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        System.out.println("Добавление клиента: " + customer.getCustomerName() + " " + customer.getCustomerLastName());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getCustomerLastName());
            stmt.setString(3, customer.getCustomerSurname());
            stmt.setString(4, customer.getCustomerAddress());
            stmt.setString(5, customer.getCustomerPhoneNumber());
            stmt.setString(6, customer.getCustomerEmail());

            int result = stmt.executeUpdate();
            System.out.println("Результат добавления: " + result + " строк");

            return result > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении клиента: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Обновить клиента
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET customer_name = ?, customer_lastName = ?, customer_surname = ?, " +
                "customer_address = ?, customer_phoneNumber = ?, customer_email = ? " +
                "WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getCustomerLastName());
            stmt.setString(3, customer.getCustomerSurname());
            stmt.setString(4, customer.getCustomerAddress());
            stmt.setString(5, customer.getCustomerPhoneNumber());
            stmt.setString(6, customer.getCustomerEmail());
            stmt.setInt(7, customer.getCustomerId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Удалить клиента
    // Удалить клиента
    public boolean deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE customer_id = ?";

        System.out.println("Попытка удаления клиента с ID: " + id);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            System.out.println("Результат удаления: " + result + " строк");

            return result > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении клиента: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Получить общее количество клиентов
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

    // Преобразование ResultSet в объект Customer
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