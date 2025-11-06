package com.school.web;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class Db {
    private static volatile DataSource DS;

    private Db() {}

    public static DataSource ds() {
        DataSource local = DS;
        if (local == null) {
            synchronized (Db.class) {
                local = DS;
                if (local == null) {
                    DS = local = lookup();
                }
            }
        }
        return local;
    }

    private static DataSource lookup() {
        try {
            // Ищем java:comp/env/jdbc/SchoolDb — это «полное» имя для ресурса из web.xml
            return (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/SchoolDb");
        } catch (NamingException e) {
            throw new IllegalStateException("JNDI DataSource 'jdbc/SchoolDb' not found", e);
        }
    }
}
