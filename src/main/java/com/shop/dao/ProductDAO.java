package com.shop.dao;

import com.shop.model.Product;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Получить все товары с пагинацией
    public List<Product> getAllProducts(int offset, int limit) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product ORDER BY product_id LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    // Поиск товаров с фильтрацией
    public List<Product> searchProducts(String name, String code, Boolean available,
                                        Double minPrice, Double maxPrice,
                                        int offset, int limit) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND product_name LIKE ?");
            params.add("%" + StringEscapeUtils.escapeJava(name) + "%");
        }
        if (code != null && !code.trim().isEmpty()) {
            sql.append(" AND code LIKE ?");
            params.add("%" + StringEscapeUtils.escapeJava(code) + "%");
        }
        if (available != null) {
            sql.append(" AND product_available = ?");
            params.add(available);
        }
        if (minPrice != null) {
            sql.append(" AND product_price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND product_price <= ?");
            params.add(maxPrice);
        }

        sql.append(" ORDER BY product_id LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    // Получить товар по ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        }
        return null;
    }

    // Добавить товар
    public boolean addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product (code, product_name, product_price, product_length, " +
                "product_quantity, product_available, product_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StringEscapeUtils.escapeJava(product.getCode()));
            stmt.setString(2, StringEscapeUtils.escapeJava(product.getProductName()));
            stmt.setBigDecimal(3, product.getProductPrice());
            stmt.setBigDecimal(4, product.getProductLength());
            stmt.setInt(5, product.getProductQuantity());
            stmt.setBoolean(6, product.getProductAvailable());
            stmt.setString(7, StringEscapeUtils.escapeJava(product.getProductDescription()));

            return stmt.executeUpdate() > 0;
        }
    }

    // Обновить товар
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET code = ?, product_name = ?, product_price = ?, " +
                "product_length = ?, product_quantity = ?, product_available = ?, " +
                "product_description = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StringEscapeUtils.escapeJava(product.getCode()));
            stmt.setString(2, StringEscapeUtils.escapeJava(product.getProductName()));
            stmt.setBigDecimal(3, product.getProductPrice());
            stmt.setBigDecimal(4, product.getProductLength());
            stmt.setInt(5, product.getProductQuantity());
            stmt.setBoolean(6, product.getProductAvailable());
            stmt.setString(7, StringEscapeUtils.escapeJava(product.getProductDescription()));
            stmt.setInt(8, product.getProductId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Удалить товар
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM Product WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Получить общее количество товаров
    public int getTotalProductsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Product";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Статистика по товарам
    public ProductStatistics getProductStatistics() throws SQLException {
        ProductStatistics stats = new ProductStatistics();
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(product_quantity) as total_quantity, " +
                "AVG(product_price) as avg_price, " +
                "MAX(product_price) as max_price, " +
                "MIN(product_price) as min_price, " +
                "COUNT(CASE WHEN product_available = 1 THEN 1 END) as available_count " +
                "FROM Product";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                stats.setTotalProducts(rs.getInt("total"));
                stats.setTotalQuantity(rs.getInt("total_quantity"));
                stats.setAveragePrice(rs.getBigDecimal("avg_price"));
                stats.setMaxPrice(rs.getBigDecimal("max_price"));
                stats.setMinPrice(rs.getBigDecimal("min_price"));
                stats.setAvailableProducts(rs.getInt("available_count"));
            }
        }
        return stats;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("product_id"),
                rs.getString("code"),
                rs.getString("product_name"),
                rs.getBigDecimal("product_price"),
                rs.getBigDecimal("product_length"),
                rs.getInt("product_quantity"),
                rs.getBoolean("product_available"),
                rs.getString("product_description")
        );
    }

    public static class ProductStatistics {
        private int totalProducts = 0;
        private int totalQuantity = 0;
        private int availableProducts = 0;
        private java.math.BigDecimal averagePrice = java.math.BigDecimal.ZERO;
        private java.math.BigDecimal maxPrice = java.math.BigDecimal.ZERO;
        private java.math.BigDecimal minPrice = java.math.BigDecimal.ZERO;

        // Конструктор по умолчанию
        public ProductStatistics() {}

        // Геттеры и сеттеры
        public int getTotalProducts() { return totalProducts; }
        public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

        public int getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

        public int getAvailableProducts() { return availableProducts; }
        public void setAvailableProducts(int availableProducts) { this.availableProducts = availableProducts; }

        public java.math.BigDecimal getAveragePrice() { return averagePrice; }
        public void setAveragePrice(java.math.BigDecimal averagePrice) { this.averagePrice = averagePrice; }

        public java.math.BigDecimal getMaxPrice() { return maxPrice; }
        public void setMaxPrice(java.math.BigDecimal maxPrice) { this.maxPrice = maxPrice; }

        public java.math.BigDecimal getMinPrice() { return minPrice; }
        public void setMinPrice(java.math.BigDecimal minPrice) { this.minPrice = minPrice; }
    }
}