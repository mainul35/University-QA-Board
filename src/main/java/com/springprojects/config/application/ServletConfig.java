package com.springprojects.config.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

import javax.servlet.ServletContext;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.springprojects.controller"})
public class ServletConfig extends WebMvcConfigurerAdapter {

    @Autowired
    ThymeleafViewResolver viewResolver;

//---------------------------- For Mobile device support------------------------
    @Bean
    public DeviceResolverHandlerInterceptor
    deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver
    deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Bean
    public SitePreferenceHandlerInterceptor
    sitePreferenceHandlerInterceptor() {
        return new SitePreferenceHandlerInterceptor();
    }

    @Bean
    public SitePreferenceHandlerMethodArgumentResolver
    sitePreferenceHandlerMethodArgumentResolver() {
        return new SitePreferenceHandlerMethodArgumentResolver();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceResolverHandlerInterceptor());
        registry.addInterceptor(sitePreferenceHandlerInterceptor());
    }


    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(sitePreferenceHandlerMethodArgumentResolver());
    }


// For resolving JSP pages
//    @Bean
//    public InternalResourceViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }

    @Bean
    public LiteDeviceDelegatingViewResolver liteDeviceDelegatingViewResolver(){
        LiteDeviceDelegatingViewResolver resolver =
                new LiteDeviceDelegatingViewResolver(viewResolver);
        resolver.setMobilePrefix("mobile/");
        resolver.setTabletPrefix("tablet/");
        return resolver;
    }


//-----------------------Thymeleaf View resolver --------------------------------


    @Bean
    @Description("Thymeleaf View Resolver")
    public ThymeleafViewResolver viewResolver(ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode("LEGACYHTML5");
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new SpringSecurityDialect());
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCache(false);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }

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
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler(
        		"/admin/resources/**",
        		"/staff/resources/**",
        		"/student/resources/**",
        		"/qa_manager/resources/**",
        		"/qa_coordinator/resources/**",
        		"/resources/**"
        		).addResourceLocations("/resources/");
    }

}
