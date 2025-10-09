package com.shop.controller;

import com.shop.dao.OrderDAO;
import com.shop.model.Order;

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
            switch (action) {
                case "search":
                    searchOrders(request, response);
                    break;
                default:
                    listOrders(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ошибка базы данных: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ошибка приложения: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
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
            e.printStackTrace();
            request.setAttribute("error", "Ошибка базы данных: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ошибка приложения: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
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

            boolean success = orderDAO.updateOrderStatus(orderId, status);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Status updated successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error updating status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
