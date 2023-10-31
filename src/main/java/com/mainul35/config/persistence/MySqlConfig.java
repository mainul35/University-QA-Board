package com.mainul35.config.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

import static com.mainul35.config.Properties.ENTITY_MANAGER_PACKAGES_TO_SCAN;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.mainul35.repository")
public class MySqlConfig {

//    @Value("${DB_URL}")
//    private String DB_URL;
//
//    @Value("${DB_USER}")
//    private String DB_USER;
//
//    @Value("${DB_PASSWORD}")
//    private String DB_PASSWORD;

    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(MySqlConfig.class);

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(com.mainul35.config.Properties.DB_DRIVER_CLASS);
        String db_url = System.getProperty("DB_URL");
        String db_username = System.getProperty("DB_USER");
        String db_password = System.getProperty("DB_PASSWORD");

        if (db_url == null) {
            log.warn("DB URL from external argument not found. Defaulting to embedded one.");
            db_url =  com.mainul35.config.Properties.DB_URL;
        }

        if (db_username == null) {
            log.warn("DB_USER from argument not found. Defaulting to embedded one.");
            db_username =  com.mainul35.config.Properties.DB_USER;
        }

        if (db_password == null) {
            log.warn("DB_PASSWORD from argument not found. Defaulting to embedded one.");
            db_password =  com.mainul35.config.Properties.DB_PASSWORD;
        }

        dataSource.setUrl(db_url);     //MySQL Specific: +"?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false"
        dataSource.setUsername(db_username);
        dataSource.setPassword(db_password);
        System.out.println("================== Connection Properties ================");
        System.out.println(db_url);
        System.out.println(db_username);
        System.out.println(db_password);
        System.out.println("================== Connection Properties ================");

        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Primary
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(ENTITY_MANAGER_PACKAGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        String db_url = System.getenv("DB_URL");
        if (db_url == null) {
            log.warn("DB URL from external argument not found. Defaulting to embedded one.");
            db_url =  com.mainul35.config.Properties.DB_URL;
        }
        hibernateProperties.put("jakarta.persistence.jdbc.url", db_url);
        hibernateProperties.put("hibernate.dialect", com.mainul35.config.Properties.HIBERNATE_DIALECT);
        hibernateProperties.put("hibernate.show_sql", com.mainul35.config.Properties.HIBERNATE_SHOW_SQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", com.mainul35.config.Properties.HBM2DDL_AUTO);
        hibernateProperties.put("hibernate.connection.autocommit", false);
        hibernateProperties.put("hibernate.enable_lazy_load_no_trans", true);
        sessionFactoryBean.setHibernateProperties(hibernateProperties);

        return sessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory());
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(ENTITY_MANAGER_PACKAGES_TO_SCAN);
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }
}
