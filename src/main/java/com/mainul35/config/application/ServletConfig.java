package com.mainul35.config.application;

import com.mainul35.config.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.mainul35.controller"})
public class ServletConfig implements WebMvcConfigurer {

    @Bean
    @Autowired
    public ThymeleafViewResolver viewResolver(ApplicationContext applicationContext){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine(applicationContext));
        return viewResolver;
    }


//-----------------------Thymeleaf View resolver --------------------------------

    private SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext) {

        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(true);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new SpringSecurityDialect());
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCache(false);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return templateResolver;
    }

    private SpringTemplateEngine templateEngine(ApplicationContext applicationContext){
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver(applicationContext));
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
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
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(999999999);
        registry.addResourceHandler(
        		"/admin/resources/**",
        		"/staff/resources/**",
        		"/student/resources/**",
        		"/qa_manager/resources/**",
        		"/qa_coordinator/resources/**",
        		"/qa_coordinator/ideas/resources/**",
        		"/ideas/resources/**",
        		"/resources/**"
        		).addResourceLocations("/resources/").setCachePeriod(999999999);
        registry.addResourceHandler(
        		"/temp/**", 
        		"/student/temp/**",
        		"/admin//temp/**", 
        		"/staff/temp/**", 
        		"/qa_manager/temp/**", 
        		"/qa_coordinator/temp/**", 
        		"/ideas/temp/**"
        		).addResourceLocations("file:///" + Properties.WRITE_PATH + "/").setCachePeriod(999999999);
    }

}
