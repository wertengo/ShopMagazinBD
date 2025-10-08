package com.shop;

import java.sql.*;
import java.util.Random;

public class DatabaseDataInserter {

    private static final String URL = "jdbc:mysql://localhost:3306/shopbd";
    private static final String USER = "root";
    private static final String PASSWORD = "gfhjkm";

    // Категории товаров для разнообразия
    private static final String[] CATEGORIES = {
            "Ноутбук", "Смартфон", "Телевизор", "Наушники", "Планшет",
            "Монитор", "Клавиатура", "Мышь", "Фотоаппарат", "Принтер",
            "Смарт-часы", "Колонка", "Роутер", "Внешний жесткий диск", "Игровая консоль"
    };

    // Бренды товаров
    private static final String[] BRANDS = {
            "Samsung", "Apple", "Sony", "LG", "Asus", "Lenovo", "Xiaomi", "Huawei",
            "Dell", "HP", "Acer", "Logitech", "Razer", "Canon", "Nikon", "TP-Link"
    };

    // Модели товаров
    private static final String[][] MODELS = {
            {"Pro", "Air", "Ultra", "Plus", "Max", "Lite", "Standard", "Elite"},
            {"X", "S", "M", "L", "XL", "2024", "2023", "2022"},
            {"Gaming", "Office", "Home", "Professional", "Student", "Business"}
    };

    public static void main(String[] args) {
        try {
            addProductsToDatabase(1000);
            System.out.println("Успешно добавлено 1000 записей в таблицу Product!");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addProductsToDatabase(int count) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Устанавливаем соединение с базой данных
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL запрос для вставки данных
            String sql = "INSERT INTO Product (code, product_name, product_price, product_length, " +
                    "product_quantity, product_available, product_description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            Random random = new Random();

            // Добавляем записи в базу данных
            for (int i = 1; i <= count; i++) {
                // Генерируем случайные данные для товара
                String code = generateProductCode(i);
                String name = generateProductName(random);
                double price = generatePrice(random);
                double length = generateLength(random);
                int quantity = generateQuantity(random);
                boolean available = random.nextBoolean();
                String description = generateDescription(random, name);

                // Устанавливаем параметры в PreparedStatement
                statement.setString(1, code);
                statement.setString(2, name);
                statement.setDouble(3, price);
                statement.setDouble(4, length);
                statement.setInt(5, quantity);
                statement.setBoolean(6, available);
                statement.setString(7, description);

                // Добавляем в batch для оптимизации
                statement.addBatch();

                // Выполняем batch каждые 100 записей
                if (i % 100 == 0) {
                    statement.executeBatch();
                    System.out.println("Добавлено " + i + " записей...");
                }
            }

            // Выполняем оставшиеся записи
            statement.executeBatch();

        } finally {
            // Закрываем ресурсы
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    // Генерация кода товара
    private static String generateProductCode(int index) {
        return String.format("PRD%06d", index);
    }

    // Генерация названия товара
    private static String generateProductName(Random random) {
        String brand = BRANDS[random.nextInt(BRANDS.length)];
        String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
        String model1 = MODELS[0][random.nextInt(MODELS[0].length)];
        String model2 = MODELS[1][random.nextInt(MODELS[1].length)];

        return brand + " " + category + " " + model1 + " " + model2;
    }

    // Генерация цены (от 500 до 50000 рублей)
    private static double generatePrice(Random random) {
        return Math.round(500 + random.nextDouble() * 49500 * 100.0) / 100.0;
    }

    // Генерация длины товара (от 5 до 200 см)
    private static double generateLength(Random random) {
        return Math.round((5 + random.nextDouble() * 195) * 10.0) / 10.0;
    }

    // Генерация количества на складе (от 0 до 1000)
    private static int generateQuantity(Random random) {
        return random.nextInt(1001);
    }

    // Генерация описания товара
    private static String generateDescription(Random random, String productName) {
        String[] features = {
                "Высокое качество сборки", "Энергоэффективный", "Стильный дизайн",
                "Простота использования", "Инновационные технологии", "Надежность",
                "Быстрая работа", "Отличное соотношение цены и качества", "Премиум класс"
        };

        String feature1 = features[random.nextInt(features.length)];
        String feature2 = features[random.nextInt(features.length)];

        return productName + ". " + feature1 + ". " + feature2 + ". Подробное описание товара.";
    }
}