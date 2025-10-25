package com.shop.controller;

import com.shop.dao.CustomerDAO;
import com.shop.model.Customer;
import com.shop.util.SecurityLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO;
    private static final String CSRF_TOKEN_NAME = "csrfToken";

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

        // CSRF защита
        if (!"search".equals(request.getParameter("action"))) {
            String sessionToken = (String) request.getSession().getAttribute(CSRF_TOKEN_NAME);
            String requestToken = request.getParameter("csrfToken");

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                SecurityLogger.logSecurityEvent("CSRF_ATTEMPT",
                        "IP: " + request.getRemoteAddr() + ", Session: " + request.getSession().getId());
                response.sendRedirect("customers?error=Security+violation");
                return;
            }
        }

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

        // Генерация CSRF токена
        String csrfToken = generateCSRFToken();
        request.getSession().setAttribute(CSRF_TOKEN_NAME, csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Добавление клиента");
        request.setAttribute("contentPage", "/views/customer-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        // Генерация CSRF токена
        String csrfToken = generateCSRFToken();
        request.getSession().setAttribute(CSRF_TOKEN_NAME, csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerDAO.getCustomerById(id);
        request.setAttribute("customer", customer);
        request.setAttribute("activePage", "customers");
        request.setAttribute("pageTitle", "Редактирование клиента");
        request.setAttribute("contentPage", "/views/customer-form-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }

    private String generateCSRFToken() {
        return UUID.randomUUID().toString();
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        System.out.println("=== НАЧАЛО INSERT CUSTOMER ===");

        try {
            Customer customer = extractCustomerFromRequest(request);
            System.out.println("Данные клиента получены: " + customer.getCustomerName() + " " + customer.getCustomerLastName());

            boolean success = customerDAO.addCustomer(customer);
            System.out.println("Результат добавления в БД: " + success);

            if (success) {
                System.out.println("Успешное добавление, перенаправляем на /customers");
                // ИСПОЛЬЗУЙТЕ АНГЛИЙСКИЙ ТЕКСТ ДЛЯ ПАРАМЕТРОВ
                response.sendRedirect("customers?message=Customer+added+successfully");
                System.out.println("Перенаправление выполнено");
                return;
            } else {
                System.out.println("Ошибка при добавлении в БД");
                request.setAttribute("error", "Ошибка при добавлении клиента");
            }
        } catch (Exception e) {
            System.err.println("ОШИБКА в insertCustomer: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ошибка при добавлении клиента: " + e.getMessage());
        }

        // Если дошли сюда, значит была ошибка - показываем форму снова
        System.out.println("Показываем форму с ошибкой");
        showNewForm(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Customer customer = extractCustomerFromRequest(request);
            customer.setCustomerId(id);
            boolean success = customerDAO.updateCustomer(customer);

            if (success) {
                // АНГЛИЙСКИЙ ТЕКСТ
                response.sendRedirect("customers?message=Customer+updated+successfully");
            } else {
                request.setAttribute("error", "Ошибка при обновлении клиента");
                request.setAttribute("customer", customer);
                showEditForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ошибка при обновлении клиента: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        System.out.println("=== НАЧАЛО DELETE CUSTOMER ===");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("Удаление клиента с ID: " + id);

            boolean success = customerDAO.deleteCustomer(id);
            System.out.println("Результат удаления из БД: " + success);

            if (success) {
                System.out.println("Успешное удаление, перенаправляем на /customers");
                // АНГЛИЙСКИЙ ТЕКСТ
                response.sendRedirect("customers?message=Customer+deleted+successfully");
                System.out.println("Перенаправление выполнено");
            } else {
                System.out.println("Ошибка при удалении из БД");
                response.sendRedirect("customers?error=Delete+failed");
            }
        } catch (Exception e) {
            System.err.println("ОШИБКА в deleteCustomer: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("customers?error=Delete+error");
        }
    }

    private Customer extractCustomerFromRequest(HttpServletRequest request) {
        Customer customer = new Customer();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String surname = request.getParameter("surname");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        // Валидация данных
        if (!isValidName(firstName) || !isValidName(lastName) ||
                !isValidEmail(email) || !isValidPhone(phone)) {
            SecurityLogger.logSecurityEvent("VALIDATION_FAILED",
                    "IP: " + request.getRemoteAddr() + ", Data: " + firstName + "," + lastName + "," + email);
            throw new IllegalArgumentException("Invalid input data");
        }

        customer.setCustomerName(firstName.trim());
        customer.setCustomerLastName(lastName.trim());
        customer.setCustomerSurname(surname != null ? surname.trim() : "");
        customer.setCustomerAddress(address != null ? address.trim() : "");
        customer.setCustomerPhoneNumber(phone.trim());
        customer.setCustomerEmail(email.trim());

        return customer;
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Zа-яА-ЯёЁ\\s-]{2,50}");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("[+0-9\\s()-]{10,20}");
    }
}