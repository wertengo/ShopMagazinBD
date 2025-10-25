package com.shop.controller;

import com.shop.dao.OrderDAO;
import com.shop.model.Order;
import com.shop.util.SecurityLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            if ("search".equals(action)) {
                searchOrders(request, response);
            } else {
                listOrders(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            throw new ServletException("Application error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("updateStatus".equals(action)) {
                updateOrderStatus(request, response);
            } else if ("search".equals(action)) {
                searchOrders(request, response);
            } else {
                listOrders(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            throw new ServletException("Application error", e);
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<Order> orders = orderDAO.getAllOrders(offset, recordsPerPage);
        int totalRecords = orderDAO.getTotalOrdersCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("orders", orders);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("activePage", "orders");
        request.setAttribute("pageTitle", "Управление заказами");
        request.setAttribute("contentPage", "/views/orders-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void searchOrders(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String orderNumber = request.getParameter("orderNumber");
        String status = request.getParameter("status");
        String customerName = request.getParameter("customerName");

        int page = 1;
        int recordsPerPage = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<Order> orders = orderDAO.searchOrders(orderNumber, status, customerName, offset, recordsPerPage);
        int totalRecords = orderDAO.getTotalOrdersCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("orders", orders);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchOrderNumber", orderNumber);
        request.setAttribute("searchStatus", status);
        request.setAttribute("searchCustomerName", customerName);
        request.setAttribute("activePage", "orders");
        request.setAttribute("pageTitle", "Управление заказами");
        request.setAttribute("contentPage", "/views/orders-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            // Дополнительная валидация
            if (!isValidStatus(status)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid status");
                return;
            }

            boolean success = orderDAO.updateOrderStatus(orderId, status);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Status updated successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error updating status");
            }
        } catch (Exception e) {
            SecurityLogger.logSecurityEvent("ORDER_STATUS_UPDATE_FAILED",
                    "IP: " + request.getRemoteAddr() + ", Order: " + request.getParameter("id"));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    private boolean isValidStatus(String status) {
        String[] allowedStatuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
        for (String allowed : allowedStatuses) {
            if (allowed.equals(status)) {
                return true;
            }
        }
        return false;
    }

}
