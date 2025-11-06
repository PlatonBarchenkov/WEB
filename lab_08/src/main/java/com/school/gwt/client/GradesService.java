package com.school.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.school.gwt.shared.GradeRowDto;
import java.util.List;

@RemoteServiceRelativePath("gwt/grades")
public interface GradesService extends RemoteService {
    List<GradeRowDto> findAll();
    void insert(String student, String subject, int grade);
}
