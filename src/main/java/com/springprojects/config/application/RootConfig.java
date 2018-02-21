package com.springprojects.config.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.springprojects.config.Utils;

@Configuration
@ComponentScan(basePackages = {"com.springprojects.service"})
public class RootConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }
    
    @Bean
    public Utils utils() {
    	return new Utils();
    }
}
