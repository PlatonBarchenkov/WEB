package com.school.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.Statement;

public final class DbInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection con = Db.ds().getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate("""
                create table if not exists grades(
                  id identity primary key,
                  student varchar(64) not null,
                  subject varchar(64) not null,
                  grade int not null
                )
                """);
        } catch (Exception e) {
            sce.getServletContext().log("DB init failed", e);
        }
    }
    @Override public void contextDestroyed(ServletContextEvent sce) {}
}
