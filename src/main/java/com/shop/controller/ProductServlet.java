package com.shop.controller;

import com.shop.dao.ProductDAO;
import com.shop.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

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

        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Добавление товара");
        request.setAttribute("contentPage", "/views/product-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.getProductById(id);
        request.setAttribute("product", product);
        request.setAttribute("activePage", "products");
        request.setAttribute("pageTitle", "Редактирование товара");
        request.setAttribute("contentPage", "/views/product-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Product product = extractProductFromRequest(request);
        productDAO.addProduct(product);
        response.sendRedirect("products?message=Product added successfully");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Product product = extractProductFromRequest(request);
        product.setProductId(id);
        productDAO.updateProduct(product);
        response.sendRedirect("products?message=Product updated successfully");
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

        // Валидация и установка значений
        String code = request.getParameter("code");
        if (code != null && !code.trim().isEmpty()) {
            product.setCode(code.trim());
        }

        String name = request.getParameter("name");
        if (name != null && !name.trim().isEmpty()) {
            product.setProductName(name.trim());
        }

        String priceStr = request.getParameter("price");
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                product.setProductPrice(new BigDecimal(priceStr));
            } catch (NumberFormatException e) {
                product.setProductPrice(BigDecimal.ZERO);
            }
        }

        String lengthStr = request.getParameter("length");
        if (lengthStr != null && !lengthStr.isEmpty()) {
            try {
                product.setProductLength(new BigDecimal(lengthStr));
            } catch (NumberFormatException e) {
                product.setProductLength(null);
            }
        }

        String quantityStr = request.getParameter("quantity");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                product.setProductQuantity(Integer.parseInt(quantityStr));
            } catch (NumberFormatException e) {
                product.setProductQuantity(0);
            }
        }

        String availableStr = request.getParameter("available");
        if (availableStr != null && !availableStr.isEmpty()) {
            product.setProductAvailable(Boolean.parseBoolean(availableStr));
        } else {
            product.setProductAvailable(false);
        }

        String description = request.getParameter("description");
        if (description != null && !description.trim().isEmpty()) {
            product.setProductDescription(description.trim());
        }

        return product;
    }
}
