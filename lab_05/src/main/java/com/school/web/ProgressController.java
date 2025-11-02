package com.school.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/progress") // ВАЖНО: не дублируем маппинг в web.xml
public class ProgressController extends HttpServlet {

    private Map<String, List<GradeRow>> data;

    @Override public void init() {
        data = new HashMap<>();

        data.put("q1", List.of(
                new GradeRow("8A", "Ivanova", "Smirnov P.", 5),
                new GradeRow("8A", "Ivanova", "Novikova A.", 4),
                new GradeRow("8B", "Petrov",  "Kuznetsov V.", 5),
                new GradeRow("7A", "Sidorov", "Popov I.", 3)
        ));
        data.put("q2", List.of(
                new GradeRow("8A", "Ivanova", "Smirnov P.", 4),
                new GradeRow("8B", "Petrov",  "Kuznetsov V.", 5),
                new GradeRow("7A", "Sidorov", "Popov I.", 4),
                new GradeRow("9A", "Lebedeva","Ershov N.", 5)
        ));
        data.put("q3", List.of(
                new GradeRow("8A", "Ivanova", "Smirnov P.", 5),
                new GradeRow("8B", "Petrov",  "Kuznetsov V.", 4),
                new GradeRow("9A", "Lebedeva","Ershov N.", 5),
                new GradeRow("9B", "Morozov", "Andreeva K.", 5)
        ));
        data.put("q4", List.of(
                new GradeRow("8A", "Ivanova", "Smirnov P.", 5),
                new GradeRow("8A", "Ivanova", "Novikova A.", 5),
                new GradeRow("8B", "Petrov",  "Kuznetsov V.", 5),
                new GradeRow("9B", "Morozov", "Andreeva K.", 4)
        ));
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = Optional.ofNullable(req.getParameter("q")).orElse("q1");
        String lang = Optional.ofNullable(req.getParameter("lang")).orElse("ru");
        String clazzFilter = req.getParameter("class");
        String teacherFilter = req.getParameter("teacher");

        List<GradeRow> rows = new ArrayList<>(data.getOrDefault(q, List.of()));

        if (clazzFilter != null && !clazzFilter.isBlank()) {
            String f = clazzFilter.trim().toLowerCase(Locale.ROOT);
            rows = rows.stream().filter(r -> r.clazz().toLowerCase(Locale.ROOT).contains(f))
                    .collect(Collectors.toList());
        }
        if (teacherFilter != null && !teacherFilter.isBlank()) {
            String f = teacherFilter.trim().toLowerCase(Locale.ROOT);
            rows = rows.stream().filter(r -> r.teacher().toLowerCase(Locale.ROOT).contains(f))
                    .collect(Collectors.toList());
        }

        req.setAttribute("rows", rows);
        req.setAttribute("q", q);
        req.setAttribute("lang", lang);
        req.setAttribute("clazz", clazzFilter);
        req.setAttribute("teacher", teacherFilter);

        String periodKey = switch (q) {
            case "q2" -> "period.q2";
            case "q3" -> "period.q3";
            case "q4" -> "period.q4";
            default   -> "period.q1";
        };
        req.setAttribute("periodKey", periodKey);

        // ВАЖНО: путь ровно такой — файл действительно лежит в webapp/WEB-INF/views/progress.jsp
        req.getRequestDispatcher("/WEB-INF/views/progress.jsp").forward(req, resp);
    }
}
