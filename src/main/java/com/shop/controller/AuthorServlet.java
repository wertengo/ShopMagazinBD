package com.shop.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/author")
public class AuthorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("activePage", "author");
        request.setAttribute("pageTitle", "Об авторе");
        request.setAttribute("contentPage", "/views/author-content.jsp");

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }
}