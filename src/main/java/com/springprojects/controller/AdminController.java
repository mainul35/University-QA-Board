package com.springprojects.controller;

import com.springprojects.entity.UserEntity;
import com.springprojects.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    Logger logger = Logger.getLogger(AdminController.class.getName());

    @RequestMapping("/list-potential-teachers")
    public String listPotentialTeachers(Model model, HttpSession session){
//        model.addAttribute("username", session.getAttribute("username").toString());
//        List<UserEntity> userEntityList = userService.usersWithRole("ROLE_POTENTIAL_TEACHER");
//        model.addAttribute("potentialTeachers", userEntityList);
        logger.info("Showing potential teachers : ");
        return "/templates/admin/potential_teachers";
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        logger.info("Admin Dashboard : ");
        return "/admin_template/index";
    }
    
    

}
