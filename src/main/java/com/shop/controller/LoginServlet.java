package com.shop.controller;

import com.shop.dao.UserDAO;
import com.shop.util.SecurityLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        // Создаем тестового пользователя при инициализации
        try {
            userDAO.createTestUser();
        } catch (SQLException e) {
            throw new ServletException("Error creating test user", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Если запрос к корневому URL - перенаправляем на /login
//        String path = request.getServletPath();
//        if ("/".equals(path)) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }

        // Если пользователь уже авторизован, перенаправляем на dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        // Показываем страницу логина из папки views
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("login".equals(action)) {
            processLogin(request, response);
        } else if ("logout".equals(action)) {
            processLogout(request, response);
        } else {
            response.sendRedirect("login");
        }
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            if (userDAO.validateUser(username, password)) {
                // Создаем сессию
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                session.setMaxInactiveInterval(30 * 60); // 30 минут

                // Логируем успешный вход
                SecurityLogger.logSecurityEvent("LOGIN_SUCCESS",
                        "User: " + username + ", IP: " + request.getRemoteAddr());

                response.sendRedirect("dashboard");
            } else {
                // Логируем неудачную попытку входа
                SecurityLogger.logSecurityEvent("LOGIN_FAILED",
                        "User: " + username + ", IP: " + request.getRemoteAddr());

                request.setAttribute("error", "Неверный логин или пароль");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            SecurityLogger.logSecurityEvent("LOGIN_ERROR",
                    "User: " + username + ", Error: " + e.getMessage());

            request.setAttribute("error", "Ошибка базы данных");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("user");
            SecurityLogger.logSecurityEvent("LOGOUT",
                    "User: " + username + ", IP: " + request.getRemoteAddr());

            session.invalidate();
        }

        response.sendRedirect("login?message=Вы+успешно+вышли+из+системы");
    }
}