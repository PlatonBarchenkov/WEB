package com.school.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

public class ProgressServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { process(req, resp); }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { process(req, resp); }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String clazz   = request.getParameter("class");
        String teacher = request.getParameter("teacher");
        String period  = request.getParameter("period");
        String lang    = request.getParameter("lang");

        String schoolName    = getServletConfig().getInitParameter("schoolName");
        String defaultPeriod = getServletConfig().getInitParameter("defaultPeriod");
        if (period == null || period.isBlank()) period = defaultPeriod;

        Locale locale = "en".equalsIgnoreCase(lang) ? Locale.ENGLISH : new Locale("ru","RU");
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale, new UTF8Control());

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            out.println("<title>" + t(rb,"page.title") + "</title></head><body>");

            out.println("<h1>" + MessageFormat.format(t(rb,"title"), esc(period)) + "</h1>");
            out.println("<p><b>" + t(rb,"school") + ":</b> " + esc(schoolName) + "</p>");
            out.println("<p><b>" + t(rb,"clazz") + ":</b> " + (isBlank(clazz) ? t(rb,"all") : esc(clazz)) +
                    " &nbsp; <b>" + t(rb,"teacher") + ":</b> " + (isBlank(teacher) ? t(rb,"all") : esc(teacher)) + "</p>");

            out.println("<table border='1' cellspacing='0' cellpadding='6'>");
            out.println("<tr><th>" + t(rb,"student") + "</th><th>" + t(rb,"subject") + "</th><th>" + t(rb,"grade") + "</th></tr>");
            out.println("<tr><td>Иванов И.И.</td><td>" + t(rb,"subject.math") + "</td><td>5</td></tr>");
            out.println("<tr><td>Петров П.П.</td><td>" + t(rb,"subject.rus")  + "</td><td>4</td></tr>");
            out.println("<tr><td>Сидорова А.А.</td><td>" + t(rb,"subject.cs")   + "</td><td>5</td></tr>");
            out.println("</table>");

            out.println("<hr><p>"
                    + "<a href='progress?class=8A&period=" + url(t(rb,"period.q1")) + "&lang=" + (isEn(locale)?"en":"ru") + "'>"
                    + t(rb,"link.class") + "</a> | "
                    + "<a href='progress?teacher=petrov&period=" + url(t(rb,"period.q1")) + "&lang=" + (isEn(locale)?"en":"ru") + "'>"
                    + t(rb,"link.teacher") + "</a> &nbsp; | &nbsp; "
                    + "<a href='progress?lang=ru'>RU</a> / <a href='progress?lang=en'>EN</a>"
                    + "</p>");

            out.println("</body></html>");
        }
    }


    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
    private static boolean isEn(Locale l) { return Locale.ENGLISH.getLanguage().equals(l.getLanguage()); }
    private static String t(ResourceBundle rb, String key) { return rb.getString(key); }
    private static String esc(String s) { return s==null? "": s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
    private static String url(String s) { return s.replace(" ", "%20"); }

    /** Жёстко читаем .properties в UTF-8 */
    public static class UTF8Control extends ResourceBundle.Control {
        @Override public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                                  ClassLoader loader, boolean reload) throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (InputStream is = loader.getResourceAsStream(resourceName)) {
                if (is == null) return null;
                try (InputStreamReader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    return new PropertyResourceBundle(r);
                }
            }
        }
    }
}
