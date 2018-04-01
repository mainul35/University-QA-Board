package com.springprojects.config.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.springprojects.config.Utils;

@Configuration
@ComponentScan(basePackages = {"com.springprojects.service", "com.springprojects.config"})
public class RootConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }
}
