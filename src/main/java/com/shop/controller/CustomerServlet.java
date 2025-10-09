package com.shop.controller;

import com.shop.dao.CustomerDAO;
import com.shop.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        customerDAO = new CustomerDAO();
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
                    deleteCustomer(request, response);
                    break;
                default:
                    listCustomers(request, response);
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
                    insertCustomer(request, response);
                    break;
                case "update":
                    updateCustomer(request, response);
                    break;
                case "search":
                    searchCustomers(request, response);
                    break;
                default:
                    listCustomers(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<Customer> customers = customerDAO.getAllCustomers(offset, recordsPerPage);
        int totalRecords = customerDAO.getTotalCustomersCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("customers", customers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Управление клиентами");
        request.setAttribute("contentPage", "/views/customers-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void searchCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        int page = 1;
        int recordsPerPage = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<Customer> customers = customerDAO.searchCustomers(name, email, phone, offset, recordsPerPage);
        int totalRecords = customerDAO.getTotalCustomersCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("customers", customers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("searchName", name);
        request.setAttribute("searchEmail", email);
        request.setAttribute("searchPhone", phone);
        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Управление клиентами");
        request.setAttribute("contentPage", "/views/customers-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Добавление клиента");
        request.setAttribute("contentPage", "/views/customer-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerDAO.getCustomerById(id);
        request.setAttribute("customer", customer);
        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Редактирование клиента");
        request.setAttribute("contentPage", "/views/customer-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Customer customer = extractCustomerFromRequest(request);
        boolean success = customerDAO.addCustomer(customer);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/customers?message=Клиент успешно добавлен");
        } else {
            response.sendRedirect(request.getContextPath() + "/customers?error=Ошибка при добавлении клиента");
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = extractCustomerFromRequest(request);
        customer.setCustomerId(id);
        boolean success = customerDAO.updateCustomer(customer);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/customers?message=Клиент успешно обновлен");
        } else {
            response.sendRedirect(request.getContextPath() + "/customers?error=Ошибка при обновлении клиента");
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        customerDAO.deleteCustomer(id);
        response.sendRedirect("customers?message=Customer deleted successfully");
    }

    private Customer extractCustomerFromRequest(HttpServletRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getParameter("firstName"));
        customer.setCustomerLastName(request.getParameter("lastName"));
        customer.setCustomerSurname(request.getParameter("surname"));
        customer.setCustomerAddress(request.getParameter("address"));
        customer.setCustomerPhoneNumber(request.getParameter("phone"));
        customer.setCustomerEmail(request.getParameter("email"));
        return customer;
    }
}