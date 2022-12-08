package com.mainul35.config.application;

import com.mainul35.config.persistence.MySqlConfig;
import com.mainul35.config.security.CORSFilter;
import com.mainul35.config.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;


public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * Specify {@link Configuration @Configuration}
     * and/or {@link Component @Component} classes to be
     * provided to the {@linkplain #createRootApplicationContext() root application context}.
     *
     * @return the configuration classes for the root application context, or {@code null}
     * if creation and registration of a root context is not desired
     */
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class, MySqlConfig.class, SecurityConfig.class};
    }

    /**
     * Specify {@link Configuration @Configuration}
     * and/or {@link Component @Component} classes to be
     * provided to the {@linkplain #createServletApplicationContext() dispatcher servlet
     * application context}.
     *
     * @return the configuration classes for the dispatcher servlet application context or
     * {@code null} if all configuration is specified through root config classes.
     */
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ServletConfig.class};
    }

    /**
     * Specify the servlet mapping(s) for the {@code DispatcherServlet} &mdash;
     * for example {@code "/"}, {@code "/app"}, etc.
     *
     */
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * Optionally perform further registration customization once
     * {@link #registerDispatcherServlet(ServletContext)} has completed.
     *
     * @param registration the {@code DispatcherServlet} registration to be customized
     * @see #registerDispatcherServlet(ServletContext)
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("/tmp/app/uploads",
                100, 200, 50));
    }

    /**
     * Specify filters to add and map to the {@code DispatcherServlet}.
     *
     * @return an array of filters or {@code null}
     * @see #registerServletFilter(ServletContext, Filter)
     */
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new CORSFilter()};
    }
    
    
}
