package com.school.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.school.gwt.shared.GradeRowDto;
import java.util.List;

public interface GradesServiceAsync {
    void findAll(AsyncCallback<List<GradeRowDto>> cb);
    void insert(String student, String subject, int grade, AsyncCallback<Void> cb);
}
