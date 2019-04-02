package com.springprojects.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Component
public class Properties {

    public static final String DB_DRIVER_CLASS = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://ec2-50-17-246-114.compute-1.amazonaws.com:5432/d8tpv7eubuqlhv";
    public static final String DB_USER = "ndzezmaaueijjr";
    public static final String DB_PASSWORD = "0b4f23d0ff5c61debe887e4214c9a91a7d26044b073d9938dc2f82b9e72a1d47";

    public static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    public static final String HIBERNATE_SHOW_SQL = "true";
    public static final String HBM2DDL_AUTO = "update";
    public static final String ENTITY_MANAGER_PACKAGES_TO_SCAN = "com.springprojects.entity";
    
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "465";
    public static final String SMTP_CLASS = "javax.net.ssl.SSLSocketFactory";
    public static final String SMTP_AUTH = "true";

    public static String TEMP_PATH = "";
}
