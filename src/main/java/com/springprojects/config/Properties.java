package com.springprojects.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Component
public class Properties {

    public static final String DB_DRIVER_CLASS = "org.postgresql.Driver";
//    public static final String DB_URL = "jdbc:postgresql://ec2-23-23-92-204.compute-1.amazonaws.com:5432/dep3k85tqarnsr";
//    public static final String DB_USER = "drolrscduhrgip";
//    public static final String DB_PASSWORD = "e330e8f86b18b275367be467f7e14d9e45fdcfbdb736a03411a76e0b9fb83ebf";

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/qa_board";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "secret";

    public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";
    public static final String HIBERNATE_SHOW_SQL = "true";
    public static final String HBM2DDL_AUTO = "update";
    public static final String ENTITY_MANAGER_PACKAGES_TO_SCAN = "com.springprojects.entity";

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "465";
    public static final String SMTP_CLASS = "javax.net.ssl.SSLSocketFactory";
    public static final String SMTP_AUTH = "true";

    public static String TEMP_PATH = System.getProperty("user.home") + "/qa-board/temp";
    public static String WRITE_PATH = System.getProperty("user.home") + "/qa-board/temp";
    public static String READ_FROM_PATH = System.getProperty("user.home") + "/qa-board";
}
