package com.school.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
// Если сервлет замаплен как /gwt/grades у корня приложения — раскомментируй:
// import com.google.gwt.user.client.rpc.ServiceDefTarget;

import com.school.gwt.shared.GradeRowDto;
import java.util.List;

public class ProgressEntryPoint implements EntryPoint {

    private final GradesServiceAsync svc = GWT.create(GradesService.class);

    @Override
    public void onModuleLoad() {
        // ЕСЛИ mapping сервлета = "/gwt/grades", раскомментируй:
        // ((ServiceDefTarget) svc).setServiceEntryPoint(GWT.getHostPageBaseURL() + "gwt/grades");

        RootPanel app = RootPanel.get("app");

        // -------- форма добавления ----------
        TextBox tbStudent = new TextBox();
        TextBox tbSubject = new TextBox();
        TextBox tbGrade   = new TextBox();
        Button  btnAdd    = new Button("Добавить");

        HorizontalPanel form = new HorizontalPanel();
        form.setSpacing(6);
        form.add(new Label("Student:")); form.add(tbStudent);
        form.add(new Label("Subject:")); form.add(tbSubject);
        form.add(new Label("Grade:"));   form.add(tbGrade);
        form.add(btnAdd);

        // -------- таблица как в скрине ----------
        final FlexTable grid = new FlexTable();
        grid.setStyleName("grid");               // стиль с границами
        grid.setCellSpacing(0);
        grid.setCellPadding(0);

        // заголовок
        grid.setText(0,0,"ID");
        grid.setText(0,1,"Student");
        grid.setText(0,2,"Subject");
        grid.setText(0,3,"Grade");
        // делаем строку заголовка <th>
        for (int c = 0; c < 4; c++) {
            grid.getFlexCellFormatter().setStyleName(0, c, "th"); // класс th задать через CSS, либо оставь bold inline:
        }
        // проще: жирный заголовок
        grid.getRowFormatter().addStyleName(0, "header"); // не обяз.

        Button btnRefresh = new Button("Обновить");
        btnRefresh.addClickHandler(e -> refresh(grid));

        VerticalPanel wrap = new VerticalPanel();
        wrap.setSpacing(10);
        wrap.add(form);
        wrap.add(new HTML("<h3 style='margin:0'>Список оценок</h3>"));
        wrap.add(btnRefresh);
        wrap.add(grid);

        app.add(wrap);

        // обработчик "Добавить"
        btnAdd.addClickHandler(e -> {
            String s  = tbStudent.getText().trim();
            String sb = tbSubject.getText().trim();
            String gs = tbGrade.getText().trim();
            if (s.isEmpty() || sb.isEmpty() || gs.isEmpty()) { Window.alert("Заполните все поля"); return; }
            int g;
            try { g = Integer.parseInt(gs); } catch (NumberFormatException ex) { Window.alert("Оценка должна быть числом"); return; }

            svc.insert(s, sb, g, new AsyncCallback<Void>() {
                @Override public void onFailure(Throwable caught) { Window.alert("Ошибка сохранения: " + caught.getMessage()); }
                @Override public void onSuccess(Void result) {
                    tbGrade.setText("");
                    refresh(grid);
                }
            });
        });

        // первая загрузка
        refresh(grid);
    }

    private void refresh(final FlexTable grid) {
        svc.findAll(new AsyncCallback<List<GradeRowDto>>() {
            @Override public void onFailure(Throwable caught) {
                Window.alert("Ошибка загрузки: " + caught.getMessage());
            }
            @Override public void onSuccess(List<GradeRowDto> rows) {
                // очистим всё кроме шапки
                for (int r = grid.getRowCount()-1; r >= 1; r--) grid.removeRow(r);
                int r = 1;
                for (GradeRowDto d : rows) {
                    grid.setText(r,0, String.valueOf(d.id));
                    grid.setText(r,1, d.student);
                    grid.setText(r,2, d.subject);
                    grid.setText(r,3, String.valueOf(d.grade));
                    r++;
                }
            }
        });
    }
}
