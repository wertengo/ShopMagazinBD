package com.shop.controller;

import com.shop.dao.CustomerDAO;
import com.shop.dao.OrderDAO;
import com.shop.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private ProductDAO productDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Получаем статистику
            ProductDAO.ProductStatistics productStats = productDAO.getProductStatistics();
            OrderDAO.OrderStatistics orderStats = orderDAO.getOrderStatistics();
            int totalCustomers = customerDAO.getTotalCustomersCount();

            request.setAttribute("productStats", productStats);
            request.setAttribute("orderStats", orderStats);
            request.setAttribute("customerStats", new CustomerStatistics(totalCustomers));
            request.setAttribute("activePage", "dashboard");
            request.setAttribute("pageTitle", "Панель управления");
            request.setAttribute("contentPage", "/views/dashboard-content.jsp");

            request.getRequestDispatcher("/views/base.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Если есть проблемы с БД, устанавливаем значения по умолчанию
            request.setAttribute("productStats", new ProductDAO.ProductStatistics());
            request.setAttribute("orderStats", new OrderDAO.OrderStatistics());
            request.setAttribute("customerStats", new CustomerStatistics(0));
            request.setAttribute("activePage", "dashboard");
            request.setAttribute("pageTitle", "Панель управления");
            request.setAttribute("contentPage", "/views/dashboard-content.jsp");
            request.getRequestDispatcher("/views/base.jsp").forward(request, response);
        }
    }

    // Вспомогательный класс для статистики клиентов
    public static class CustomerStatistics {
        private int totalCustomers;

        public CustomerStatistics(int totalCustomers) {
            this.totalCustomers = totalCustomers;
        }

        public int getTotalCustomers() {
            return totalCustomers;
        }
    }
}