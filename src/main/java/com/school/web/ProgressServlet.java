package com.school.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String clazz   = request.getParameter("class");
        String teacher = request.getParameter("teacher");
        String period  = request.getParameter("period");

        String schoolName    = getServletConfig().getInitParameter("schoolName");
        String defaultPeriod = getServletConfig().getInitParameter("defaultPeriod");
        if (period == null || period.isBlank()) period = defaultPeriod;

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            out.println("<title>Успеваемость — " + esc(schoolName) + "</title></head><body>");
            out.println("<h1>Успеваемость за " + esc(period) + "</h1>");
            out.println("<p><b>Школа:</b> " + esc(schoolName) + "</p>");
            out.println("<p><b>Класс:</b> " + (clazz == null ? "все" : esc(clazz)) +
                    " &nbsp; <b>Преподаватель:</b> " + (teacher == null ? "все" : esc(teacher)) + "</p>");
            out.println("<table border='1' cellspacing='0' cellpadding='6'>");
            out.println("<tr><th>Ученик</th><th>Предмет</th><th>Оценка</th></tr>");
            out.println("<tr><td>Иванов И.И.</td><td>Математика</td><td>5</td></tr>");
            out.println("<tr><td>Петров П.П.</td><td>Русский язык</td><td>4</td></tr>");
            out.println("<tr><td>Сидорова А.А.</td><td>Информатика</td><td>5</td></tr>");
            out.println("</table>");
            out.println("<hr><p><a href='progress?class=8A&period=I%20четверть'>8А, I четверть</a> | " +
                    "<a href='progress?teacher=petrov&period=I%20четверть'>Учитель petrov</a></p>");
            out.println("</body></html>");
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
