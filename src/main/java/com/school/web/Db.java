package com.school.web;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public final class Db {
    private Db() {}

    public static DataSource ds() {
        try {
            Context ic = new InitialContext();
            return (DataSource) ic.lookup("java:comp/env/jdbc/SchoolDb");
        } catch (Exception e) {
            throw new IllegalStateException("JNDI lookup failed", e);
        }
    }
}
