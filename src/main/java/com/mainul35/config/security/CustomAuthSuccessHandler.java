package com.mainul35.config.security;

import com.mainul35.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private Logger logger = Logger.getLogger(CustomAuthSuccessHandler.class.getName());
    @Override
    public void onAuthenticationSuccess(
    		HttpServletRequest httpServletRequest, 
    		HttpServletResponse httpServletResponse, 
    		Authentication authentication
    		) throws IOException, ServletException {
        HttpSession session = httpServletRequest.getSession();
        UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("usr", authUser);
        session.setAttribute("id", authUser.getId());
        session.setAttribute("username", authUser.getUsername().split("@")[0]);
        session.setAttribute("authorities", authentication.getAuthorities());
        System.out.println("Authenticated user = "+authUser.toString());


        //set our response to OK status
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us to where
        //we will redirect the user after successfully login
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(httpServletRequest, httpServletResponse);
//        String requestUrl = savedRequest.getRedirectUrl();
        httpServletResponse.sendRedirect("/dashboard"); //requestUrl!=null?requestUrl:
    }
}