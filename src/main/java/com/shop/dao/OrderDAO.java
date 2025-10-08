package com.shop.dao;

import com.shop.model.Order;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getAllOrders(int offset, int limit) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(c.customer_name, ' ', c.customer_lastName) as customer_name " +
                "FROM `Order` o " +
                "LEFT JOIN Customer c ON o.customer_id = c.customer_id " +
                "ORDER BY o.order_id LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        }
        return orders;
    }

    public List<Order> searchOrders(String orderNumber, String status, String customerName,
                                    int offset, int limit) throws SQLException {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, CONCAT(c.customer_name, ' ', c.customer_lastName) as customer_name " +
                        "FROM `Order` o " +
                        "LEFT JOIN Customer c ON o.customer_id = c.customer_id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.trim().isEmpty()) {
            sql.append(" AND o.order_number LIKE ?");
            params.add("%" + StringEscapeUtils.escapeJava(orderNumber) + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND o.order_status = ?");
            params.add(StringEscapeUtils.escapeJava(status));
        }
        if (customerName != null && !customerName.trim().isEmpty()) {
            sql.append(" AND (c.customer_name LIKE ? OR c.customer_lastName LIKE ?)");
            params.add("%" + StringEscapeUtils.escapeJava(customerName) + "%");
            params.add("%" + StringEscapeUtils.escapeJava(customerName) + "%");
        }

        sql.append(" ORDER BY o.order_id LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE `Order` SET order_status = ?, order_status_changed_date = NOW() WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StringEscapeUtils.escapeJava(status));
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;
        }
    }

    public int getTotalOrdersCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM `Order`";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Статистика по заказам
    public OrderStatistics getOrderStatistics() throws SQLException {
        OrderStatistics stats = new OrderStatistics();
        String sql = "SELECT " +
                "COUNT(*) as total_orders, " +
                "SUM(order_cost) as total_revenue, " +
                "AVG(order_cost) as avg_order_value, " +
                "COUNT(CASE WHEN order_status = 'Delivered' THEN 1 END) as delivered_orders, " +
                "COUNT(CASE WHEN order_status = 'Pending' THEN 1 END) as pending_orders " +
                "FROM `Order`";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                stats.setTotalOrders(rs.getInt("total_orders"));
                stats.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                stats.setAverageOrderValue(rs.getBigDecimal("avg_order_value"));
                stats.setDeliveredOrders(rs.getInt("delivered_orders"));
                stats.setPendingOrders(rs.getInt("pending_orders"));
            }
        }
        return stats;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setOrderCost(rs.getBigDecimal("order_cost"));
        order.setOrderPaymentMethod(rs.getString("order_payment_method"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setOrderShippingAddress(rs.getString("order_shipping_address"));
        order.setOrderStatusChangedDate(rs.getTimestamp("order_status_changed_date"));
        order.setCustomerName(rs.getString("customer_name"));
        return order;
    }

    public static class OrderStatistics {
        private int totalOrders = 0;
        private int deliveredOrders = 0;
        private int pendingOrders = 0;
        private java.math.BigDecimal totalRevenue = java.math.BigDecimal.ZERO;
        private java.math.BigDecimal averageOrderValue = java.math.BigDecimal.ZERO;

        // Конструктор по умолчанию
        public OrderStatistics() {}

        // Геттеры и сеттеры
        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

        public int getDeliveredOrders() { return deliveredOrders; }
        public void setDeliveredOrders(int deliveredOrders) { this.deliveredOrders = deliveredOrders; }

        public int getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

        public java.math.BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(java.math.BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

        public java.math.BigDecimal getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(java.math.BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }
}