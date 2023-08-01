package com.mainul35.config.security;

import com.mainul35.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/resources/**", "/temp/**", "/test/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/qa_manager/**")
                        .hasRole("QA_MANAGER")
                        .requestMatchers("/qa_coordinator/**")
                        .hasRole("QA_COORDINATOR")
                        .requestMatchers("/staff/**")
                        .hasRole("STAFF")
                        .requestMatchers("/student/**")
                        .hasRole("STUDENT")
                        .requestMatchers(
                                "/dashboard",
                                "/ideas",
                                "/ideas/**",
                                "/download**",
                                "/profile**",
                                "/footer",
                                "/main-header",
                                "/post-new-idea",
                                "/control-sidebar"
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
