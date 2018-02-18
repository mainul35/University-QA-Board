package com.springprojects.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springprojects.entity.UserEntity;

@Controller
@RequestMapping("/student")
public class StudentController {

	Logger logger = Logger.getLogger(getClass().getName());
	
	@RequestMapping(method = RequestMethod.GET, value = "/dashboard")
	public String studentDashboard_GET(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        logger.info("Admin Dashboard : ");
		return "/student_template/index";
	}
}
