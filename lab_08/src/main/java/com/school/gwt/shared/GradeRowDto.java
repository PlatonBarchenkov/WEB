package com.school.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GradeRowDto implements IsSerializable {
    public long id;
    public String student;
    public String subject;
    public int grade;

    public GradeRowDto() { } // обязателен для RPC

    public GradeRowDto(long id, String student, String subject, int grade) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.grade = grade;
    }
}
