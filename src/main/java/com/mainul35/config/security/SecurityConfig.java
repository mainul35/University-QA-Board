package com.mainul35.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // The requestMatcher codes have been changed to deal with the error:
    // IllegalArgumentException: This method cannot decide whether these patterns are Spring MVC patterns or not. If this endpoint is a Spring MVC endpoint, please use requestMatchers(MvcRequestMatcher); otherwise, please use requestMatchers(AntPathRequestMatcher)
    // Solution:
    // Follow answer to this stackoverflow question: https://stackoverflow.com/questions/76809698/spring-security-method-cannot-decide-pattern-is-mvc-or-not-spring-boot-applicati
    // And this github issue sample link: https://github.com/spring-projects/spring-security-samples/commit/4e3bec904a5467db28ea33e25ac9d90524b53d66
    @Scope("prototype")
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(mvc.pattern("/contents/**"), mvc.pattern("/temp/**") , mvc.pattern("/test/**")).permitAll()
                        .requestMatchers(mvc.pattern("/"), mvc.pattern("/login")).permitAll()
                        .requestMatchers(mvc.pattern("/admin/**"))
                        .hasRole("ADMIN")
                        .requestMatchers(mvc.pattern("/qa_manager/**"))
                        .hasRole("QA_MANAGER")
                        .requestMatchers(mvc.pattern("/qa_coordinator/**"))
                        .hasRole("QA_COORDINATOR")
                        .requestMatchers(mvc.pattern("/staff/**"))
                        .hasRole("STAFF")
                        .requestMatchers(mvc.pattern("/student/**"))
                        .hasRole("STUDENT")
                        .requestMatchers(
                                mvc.pattern("/dashboard"),
                                mvc.pattern("/ideas"),
                                mvc.pattern("/ideas/**"),
                                mvc.pattern("/download**"),
                                mvc.pattern("/profile**"),
                                mvc.pattern("/footer"),
                                mvc.pattern("/main-header"),
                                mvc.pattern("/post-new-idea"),
                                mvc.pattern("/control-sidebar")
                        )
                        .authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.loginPage("/login")
                                .loginProcessingUrl("/login-processing")
                                .usernameParameter("email")
                                .passwordParameter("password")
//                .defaultSuccessUrl("/")
                                .successHandler(authSuccessHandler())
                                .permitAll()
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/403"));

        return http.build();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
////#####################                   In Memory Authentication        #####################
////        auth
////                .inMemoryAuthentication()
////                .withUser("admin").password("password").roles("ADMIN")
////                .and()
////                .withUser("user").password("password").roles("USER");
//
////#####################                   Jdbc Authentication             #####################
//
////        auth.jdbcAuthentication().dataSource(dataSource)
////                .usersByUsernameQuery("select u.`username`, u.`password`, u.`enabled` from `user` u where u.username=? ")
////                .authoritiesByUsernameQuery(
////                        "select u.`username`, a.`authority` from `user` u, `authority` a, `user_autrhority` ua where u.id = ua.user_id and a.id = ua.authority_id and u.username=? ");
//
//
////#####################     Custom UserDetailsService Authentication      #####################
//
//        auth.userDetailsService(userService).passwordEncoder(BCPasswordEncoder());
//    }

    @Bean
    public CustomAuthSuccessHandler authSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    @Bean(name = "passwordEncoder")
    PasswordEncoder BCPasswordEncoder(){
        return new BCryptPasswordEncoder(11);
    }
}
