package com.mainul35.config.application;

import com.mainul35.config.Initializer;
import com.mainul35.config.persistence.MySqlConfig;
import com.mainul35.service.AttachmentService;
import com.mainul35.service.AuthorityService;
import com.mainul35.service.DepartmentService;
import com.mainul35.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Import({MySqlConfig.class})
@ComponentScan(basePackages = {"com.mainul35"})
public class Main extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PasswordEncoder encoder;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Main.class);
    }


    @Override
    public void run(String... args) throws Exception {
        Initializer initializer = new Initializer(authorityService, userService, attachmentService,
                departmentService, encoder);
    }
}
