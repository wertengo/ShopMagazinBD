package com.shop.dao;

import com.shop.model.User;
import com.shop.util.SecurityLogger;

import java.sql.*;

public class UserDAO {

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM User WHERE username = ? AND active = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getBoolean("active")
                );
            }
        }
        return null;
    }

    public boolean validateUser(String username, String password) throws SQLException {
        User user = getUserByUsername(username);
        if (user != null) {
            // В реальном приложении используйте хеширование паролей!
            // Здесь для простоты используется plain text
            return user.getPassword().equals(password);
        }
        return false;
    }

    // Создание тестового пользователя (выполнить один раз)
    public void createTestUser() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM User WHERE username = 'admin'";
        String insertSql = "INSERT INTO User (username, password, role, active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             ResultSet rs = checkStmt.executeQuery()) {

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "admin");
                    insertStmt.setString(2, "admin123"); // В реальном приложении используйте хеширование!
                    insertStmt.setString(3, "ADMIN");
                    insertStmt.setBoolean(4, true);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
}