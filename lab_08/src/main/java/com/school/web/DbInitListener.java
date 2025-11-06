package com.school.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

public class DbInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ensureSchema();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // ничего
    }

    private static volatile boolean schemaOk = false;

    private void ensureSchema() {
        if (schemaOk) return;
        synchronized (DbInitListener.class) {
            if (schemaOk) return;
            try (Connection con = Db.ds().getConnection();
                 Statement st = con.createStatement()) {

                // если у тебя были text blocks ("""), замени на обычные строки:
                String ddl =
                        "create table if not exists GRADES (\n" +
                                "  id bigint auto_increment primary key,\n" +
                                "  student varchar(255) not null,\n" +
                                "  subject varchar(255) not null,\n" +
                                "  grade int not null\n" +
                                ");";
                st.executeUpdate(ddl);

                schemaOk = true;
            } catch (Exception e) {
                throw new RuntimeException("DB schema init failed", e);
            }
        }
    }
}
