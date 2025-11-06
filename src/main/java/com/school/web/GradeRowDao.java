package com.school.web;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeRowDao {
    private static volatile boolean schemaOk = false;
    private DataSource ds() { return Db.ds(); }

    private void ensureSchema() {
        if (schemaOk) return;
        synchronized (GradeRowDao.class) {
            if (schemaOk) return;
            try (Connection con = ds().getConnection();
                 Statement st = con.createStatement()) {
                st.executeUpdate("""
                    create table if not exists grades(
                      id identity primary key,
                      student varchar(64) not null,
                      subject varchar(64) not null,
                      grade int not null
                    )
                    """);
                schemaOk = true;
            } catch (SQLException e) {
                throw new RuntimeException("Schema init failed", e);
            }
        }
    }

    public List<GradeRow> findAll() {
        ensureSchema();
        var list = new ArrayList<GradeRow>();
        try (var con = ds().getConnection();
             var st  = con.createStatement();
             var rs  = st.executeQuery(
                     "select id, student, subject, grade from grades order by id")) {
            while (rs.next()) {
                list.add(new GradeRow(
                        rs.getLong("id"),
                        rs.getString("student"),
                        rs.getString("subject"),
                        rs.getInt("grade")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void insert(String student, String subject, int grade) {
        ensureSchema();
        try (var con = ds().getConnection();
             var ps  = con.prepareStatement(
                     "insert into grades(student, subject, grade) values(?,?,?)")) {
            ps.setString(1, student);
            ps.setString(2, subject);
            ps.setInt(3, grade);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
