package com.school.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ProgressController extends HttpServlet {
    private final GradeRowDao dao = new GradeRowDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<GradeRow> rows = dao.findAll();
        req.setAttribute("rows", rows);

        // cookies (как у тебя)
        Cookie[] cookies = req.getCookies();
        String lastStudent = "";
        String lastSubject = "";
        int visits = 1;
        if (cookies != null) {
            lastStudent = Arrays.stream(cookies)
                    .filter(c -> "lastStudent".equals(c.getName()))
                    .map(c -> URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))
                    .findFirst().orElse("");
            lastSubject = Arrays.stream(cookies)
                    .filter(c -> "lastSubject".equals(c.getName()))
                    .map(c -> URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8))
                    .findFirst().orElse("");
            String v = Arrays.stream(cookies)
                    .filter(c -> "visits".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst().orElse(null);
            if (v != null) try { visits = Integer.parseInt(v) + 1; } catch (Exception ignore) {}
        }
        Cookie vc = new Cookie("visits", String.valueOf(visits));
        vc.setPath(req.getContextPath());
        vc.setMaxAge(60*60*24*30);
        vc.setHttpOnly(true);
        resp.addCookie(vc);

        req.setAttribute("lastStudent", lastStudent);
        req.setAttribute("lastSubject", lastSubject);
        req.setAttribute("visits", visits);

        req.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String student = req.getParameter("student");
        String subject = req.getParameter("subject");
        int grade = Integer.parseInt(req.getParameter("grade"));
        dao.insert(student, subject, grade);

        // cookies
        String path = req.getContextPath();
        Cookie c1 = new Cookie("lastStudent", URLEncoder.encode(student, StandardCharsets.UTF_8));
        Cookie c2 = new Cookie("lastSubject", URLEncoder.encode(subject, StandardCharsets.UTF_8));
        c1.setPath(path); c2.setPath(path);
        c1.setMaxAge(60*60*24*30); c2.setMaxAge(60*60*24*30);
        c1.setHttpOnly(true); c2.setHttpOnly(true);
        resp.addCookie(c1); resp.addCookie(c2);

        resp.sendRedirect(path + "/progress");
    }
}
