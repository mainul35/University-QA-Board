package com.springprojects.controller;

import com.springprojects.entity.Authority;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.TagService;
import com.springprojects.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    TagService tagService;

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
    
    @RequestMapping(value = "/create-role", method = RequestMethod.GET)
    public String createRole_GET(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "create-role");
        Authority authority = new Authority();
        model.addAttribute("role", authority);
    	return "/admin_template/index";
	}
    
    @RequestMapping(value = "/create-role", method = RequestMethod.POST)
    public String createRole_POST(Model model, HttpSession session, @ModelAttribute("role") Authority role){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "create-role");
        
        role.setId(System.currentTimeMillis());
        if(authorityService.create(role)==null) {
            model.addAttribute("isOk", "false");
        }else {
        	model.addAttribute("isOk", "true");
        }
        model.addAttribute("role", role);
    	return "/admin_template/index";
	}
    
    @RequestMapping(value="/view-all-roles", method=RequestMethod.GET)
    public String viewAllRoles_GET(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "view-all-roles");
        model.addAttribute("roles", authorityService.listAllAuthorities());
    	return "/admin_template/index";
	}
    
    @RequestMapping(value = "/create-tag", method = RequestMethod.GET)
    public String createTag_GET(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "create-tag");
        model.addAttribute("tag", new Tag());
        return "/admin_template/index";
    }

    @RequestMapping(value = "/create-tag", method = RequestMethod.POST)
    public String createTag_POST(Model model, HttpSession session, @ModelAttribute("tag") Tag tag){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "create-tag");
        
        tag.setTagId(System.currentTimeMillis());
        if(tagService.save(tag)==false) {
            model.addAttribute("isOk", "false");
        }else {
        	model.addAttribute("isOk", "true");
        }
		model.addAttribute("tag", tag);
    	return "/admin_template/index";
	}
    
    @RequestMapping(value="/view-all-tags", method=RequestMethod.GET)
    public String viewAllTags_GET(Model model, HttpSession session){
    	UserEntity userEntity = (UserEntity) session.getAttribute("usr");
        model.addAttribute("usr", userEntity);
        model.addAttribute("pageName", "view-all-tags");
        model.addAttribute("tags", tagService.listAllTags());
    	return "/admin_template/index";
	}
}
