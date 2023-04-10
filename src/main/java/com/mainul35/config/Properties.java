package com.mainul35.config;

import org.springframework.stereotype.Component;

@Component
public class Properties {

    public static final String DB_DRIVER_CLASS = "org.postgresql.Driver";
//    public static final String DB_URL = "jdbc:postgresql://ec2-54-205-26-79.compute-1.amazonaws.com:5432/d8m2g75h27dgi7";
//    public static final String DB_USER = "dthxxskpwprcxy";
//    public static final String DB_PASSWORD = "2de423f64ae5cc7b0e63298e2e1cb6becf030a2976904471a94eaabd9fa84e9c";
//    public static final String DB_URL = "jdbc:postgresql://localhost:5432/qa_board";
//    public static final String DB_USER = "postgres";
//    public static final String DB_PASSWORD = "secret";

    public static volatile String DB_URL = "jdbc:postgresql://192.168.1.103:5433/qabord_db";

    public static volatile String DB_USER = "postgres";

    public static volatile String DB_PASSWORD = "postgres";

    public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";
    public static final String HIBERNATE_SHOW_SQL = "true";
    public static final String HBM2DDL_AUTO = "update";
    public static final String ENTITY_MANAGER_PACKAGES_TO_SCAN = "com.mainul35.entity";

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "465";
    public static final String SMTP_CLASS = "javax.net.ssl.SSLSocketFactory";
    public static final String SMTP_AUTH = "true";

    public static String TEMP_PATH = System.getProperty("user.home") + "/qa-board/temp";
    public static String WRITE_PATH = System.getProperty("user.home") + "/qa-board/temp";
    public static String READ_FROM_PATH = System.getProperty("user.home") + "/qa-board";
}
