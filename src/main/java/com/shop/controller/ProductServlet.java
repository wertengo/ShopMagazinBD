package com.shop.controller;

import com.shop.dao.ProductDAO;
import com.shop.model.Product;
import com.shop.util.SecurityLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    private static final String CSRF_TOKEN_NAME = "csrfToken";

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                case "statistics":
                    showStatistics(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CSRF защита для всех POST кроме search
        if (!"search".equals(request.getParameter("action"))) {
            String sessionToken = (String) request.getSession().getAttribute(CSRF_TOKEN_NAME);
            String requestToken = request.getParameter("csrfToken");

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                SecurityLogger.logSecurityEvent("CSRF_ATTEMPT",
                        "IP: " + request.getRemoteAddr() + ", Session: " + request.getSession().getId());
                response.sendRedirect("products?error=Security+violation");
                return;
            }
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "insert":
                    insertProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "search":
                    searchProducts(request, response);
                    break;
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        try {
            int page = 1;
            int recordsPerPage = 10;

            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            int offset = (page - 1) * recordsPerPage;
            List<Product> products = productDAO.getAllProducts(offset, recordsPerPage);
            int totalRecords = productDAO.getTotalProductsCount();
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

            request.setAttribute("products", products);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("activePage", "products");
            request.setAttribute("pageTitle", "Управление товарами");
            request.setAttribute("contentPage", "/views/products-content.jsp");

            request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ошибка: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String availableStr = request.getParameter("available");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        Boolean available = null;
        Double minPrice = null;
        Double maxPrice = null;

        if (availableStr != null && !availableStr.isEmpty()) {
            available = Boolean.parseBoolean(availableStr);
        }
        if (minPriceStr != null && !minPriceStr.isEmpty()) {
            minPrice = Double.parseDouble(minPriceStr);
        }
        if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
            maxPrice = Double.parseDouble(maxPriceStr);
        }

        int page = 1;
        int recordsPerPage = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<Product> products = productDAO.searchProducts(name, code, available, minPrice, maxPrice, offset, recordsPerPage);
        int totalRecords = productDAO.getTotalProductsCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("products", products);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchName", name);
        request.setAttribute("searchCode", code);
        request.setAttribute("searchAvailable", availableStr);
        request.setAttribute("searchMinPrice", minPriceStr);
        request.setAttribute("searchMaxPrice", maxPriceStr);
        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Управление товарами");
        request.setAttribute("contentPage", "/views/products-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CSRF токен
        String csrfToken = generateCSRFToken();
        request.getSession().setAttribute(CSRF_TOKEN_NAME, csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Добавление товара");
        request.setAttribute("contentPage", "/views/product-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        // CSRF токен
        String csrfToken = generateCSRFToken();
        request.getSession().setAttribute(CSRF_TOKEN_NAME, csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.getProductById(id);
        request.setAttribute("product", product);
        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Редактирование товара");
        request.setAttribute("contentPage", "/views/product-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private String generateCSRFToken() {
        return UUID.randomUUID().toString();
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        try {
            Product product = extractProductFromRequest(request);
            boolean success = productDAO.addProduct(product);

            if (success) {
                response.sendRedirect("products?message=Product+added+successfully");
            } else {
                request.setAttribute("error", "Ошибка при добавлении товара");
                showNewForm(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Ошибка при добавлении товара: " + e.getMessage());
            showNewForm(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = extractProductFromRequest(request);
            product.setProductId(id);
            boolean success = productDAO.updateProduct(product);

            if (success) {
                response.sendRedirect("products?message=Product+updated+successfully");
            } else {
                request.setAttribute("error", "Ошибка при обновлении товара");
                request.setAttribute("product", product);
                showEditForm(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Ошибка при обновлении товара: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        productDAO.deleteProduct(id);
        response.sendRedirect("products?message=Product deleted successfully");
    }

    private void showStatistics(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        ProductDAO.ProductStatistics stats = productDAO.getProductStatistics();
        request.setAttribute("stats", stats);
        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Статистика товаров");
        request.setAttribute("contentPage", "/views/product-statistics-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private Product extractProductFromRequest(HttpServletRequest request) {
        Product product = new Product();

        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String lengthStr = request.getParameter("length");
        String quantityStr = request.getParameter("quantity");
        String availableStr = request.getParameter("available");
        String description = request.getParameter("description");

        // Валидация данных
        if (!isValidCode(code) || !isValidName(name)) {
            SecurityLogger.logSecurityEvent("PRODUCT_VALIDATION_FAILED",
                    "IP: " + request.getRemoteAddr() + ", Data: " + code + "," + name);
            throw new IllegalArgumentException("Invalid input data");
        }

        product.setCode(code.trim());
        product.setProductName(name.trim());

        // Обработка числовых полей
        try {
            if (priceStr != null && !priceStr.isEmpty()) {
                product.setProductPrice(new BigDecimal(priceStr));
            }
            if (lengthStr != null && !lengthStr.isEmpty()) {
                product.setProductLength(new BigDecimal(lengthStr));
            }
            if (quantityStr != null && !quantityStr.isEmpty()) {
                product.setProductQuantity(Integer.parseInt(quantityStr));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric format");
        }

        product.setProductAvailable(availableStr != null && Boolean.parseBoolean(availableStr));
        product.setProductDescription(description != null ? description.trim() : "");

        return product;
    }

    private boolean isValidCode(String code) {
        return code != null && code.matches("[a-zA-Z0-9-]{2,20}");
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Zа-яА-ЯёЁ0-9\\s-]{2,100}");
    }
}
