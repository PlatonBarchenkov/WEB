package com.school.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.school.gwt.client.GradesService;
import com.school.gwt.shared.GradeRowDto;
import com.school.web.GradeRowDao;
import com.school.web.GradeRow;

import java.util.List;
import java.util.stream.Collectors;

public class GradesServiceImpl extends RemoteServiceServlet implements GradesService {

    private final GradeRowDao dao = new GradeRowDao();

    @Override
    public List<GradeRowDto> findAll() {
        return dao.findAll().stream()
                .map(r -> new GradeRowDto(r.id(), r.student(), r.subject(), r.grade()))
                .collect(Collectors.toList());
    }

    @Override
    public void insert(String student, String subject, int grade) {
        dao.insert(student, subject, grade);
    }
}
