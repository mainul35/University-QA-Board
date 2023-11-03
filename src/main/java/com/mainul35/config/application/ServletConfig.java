package com.mainul35.config.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@ComponentScan(basePackages = {"com.mainul35.controller"})
public class ServletConfig implements WebMvcConfigurer {

    private String WRITE_PATH = System.getProperty("FILE_LOCATION") + "/qa-board/temp";

    @Bean
    @Description("Spring Message Resolver")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

//-----------------To resolve the problem: Resource interpreted as Stylesheet but transferred with MIME type text/html
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/static/contents/webjars/")
//                .setCachePeriod(999999999)
        ;
        registry.addResourceHandler(
        		"/admin/contents/**",
        		"/staff/contents/**",
        		"/student/contents/**",
        		"/qa_manager/contents/**",
        		"/qa_coordinator/contents/**",
        		"/qa_coordinator/ideas/contents/**",
        		"/ideas/contents/**",
        		"/contents/**"
        		).addResourceLocations("classpath:/static/contents/")
//                .setCachePeriod(999999999)
        ;
        registry.addResourceHandler(
        		"/temp/**",
        		"/student/temp/**",
        		"/admin//temp/**",
        		"/staff/temp/**",
        		"/qa_manager/temp/**",
        		"/qa_coordinator/temp/**",
        		"/ideas/temp/**"
        		).addResourceLocations("file:///" + WRITE_PATH + "/")
//                .setCachePeriod(999999999)
        ;
    }

}
