package com.shop.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected void forwardToPage(HttpServletRequest request, HttpServletResponse response,
                                 String contentPage, String activePage, String pageTitle)
            throws ServletException, IOException {

        request.setAttribute("activePage", activePage);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("contentPage", contentPage);

        request.getRequestDispatcher("/views/base-layout.jsp").forward(request, response);
    }
}