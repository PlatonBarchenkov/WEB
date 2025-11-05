package com.school.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        // для демонстрации кто вошёл
        String user = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "anonymous";

        try (PrintWriter out = resp.getWriter()) {
            out.println("<!doctype html><html><head><meta charset='UTF-8'><title>Progress</title></head><body>");
            out.println("<h1>Защищённая страница (/progress)</h1>");
            out.println("<p>Вы вошли как: <b>" + user + "</b></p>");
            out.println("<p><a href='" + req.getContextPath() + "/'>На главную</a></p>");
            out.println("</body></html>");
        }
    }
}
