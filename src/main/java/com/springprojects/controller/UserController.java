package com.springprojects.controller;

import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;

    @Autowired
    AttachmentService attachmentService;
    @Autowired
    PasswordEncoder encoder;
    
//    @RequestMapping(value = {"/"})
//    public String index(HttpServletRequest request){
//        System.out.println("Remote IP = "+request.getRemoteAddr());
//        System.out.println("Remote Host = "+request.getRemoteHost());
//        System.out.println("Remote Port = "+request.getRemotePort());
//        System.out.println("Remote User = "+request.getRemoteUser());
//        return "index";
//    }

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public void log(HttpServletRequest request){
        logger.info("Remote IP = "+request.getRemoteAddr());
        logger.info("Remote Host = "+request.getRemoteHost());
        logger.info("Remote Port = "+request.getRemotePort());
        logger.info("Remote User = "+request.getLocale().getDisplayCountry());
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, SitePreference sitePreference, Model model, Principal principal){
        if(principal!=null){
            return "redirect:/dashboard";
        }
        if (sitePreference == SitePreference.NORMAL) {
            log(request);
            model.addAttribute("msg", "some message");
//			authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_ADMIN"));
//			authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_QA_MANAGER"));
//			authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_QA_COORDINATOR"));
//			authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_STUDENT"));
//			UserEntity userEntity = new UserEntity();
//			userEntity.setId(System.currentTimeMillis());
//			userEntity.setName("Admin");
//			userEntity.setEmail("admin@ewsd.com");
//			userEntity.setDepartment("admin");
//			Set<Authority> authorities = new HashSet<>();
//			authorities.add(authorityService.findByRoleName("ROLE_ADMIN"));
//			userEntity.setAuthorities(authorities);
//			userEntity.setEnabled(true);
//			userEntity.setPassword(encoder.encode("secret"));
//			userEntity.setUsername("admin");
//
//			Attachment userImage = new Attachment();
//			Long attachmentId = System.currentTimeMillis();
//			userImage.setAttachmentId(attachmentId);
//			userImage.setFileName(Long.toString(attachmentId));
//			userImage.setFileTitle("Admin image");
//			userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");
//
//			attachmentService.save(userImage);
//			userEntity.setUserImage(userImage);
//			userService.createUser(userEntity);
            return "/templates/login";
        } else if (sitePreference == SitePreference.MOBILE) {
            logger.info("Site preference is mobile");
            return "/templates/login";
        } else if (sitePreference == SitePreference.TABLET) {
            logger.info("Site preference is tablet");
            return "/templates/login";
        } else {
            logger.info("no site preference");
            return "/templates/login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login_GET(Principal principal){
        if(principal!=null){
            return "redirect:/dashboard";
        }
        logger.info("Login screen : ");
        return "/templates/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login_POST(){
        return "/templates/login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp_GET(Model model, Principal principal){
        if(principal!=null){
            return "redirect:/dashboard";
        }
        UserEntity user = new UserEntity();
        model.addAttribute("id", System.currentTimeMillis());
        model.addAttribute("user", user);
        logger.info("Sign up page ");
        return "/templates/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUp_POST(
            @ModelAttribute("user")UserEntity user,
            @RequestParam("id") Long id,
            @RequestParam(name = "asTeacher", defaultValue = "false") Boolean asTeacher,
            Model model){
        user.setId(id);
        userService.createUser(user, asTeacher);
        userService.loadUserByUsername("mainuls18");
        logger.info("Attempting to login : ");
        return "/templates/signup";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard_GET(HttpSession session, Model model){
    	List<Authority> authorities = (List<Authority>)session.getAttribute("authorities");
        model.addAttribute("username", session.getAttribute("username").toString());
        for (Authority authority:authorities) {
            if(authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")){
            	logger.info("Redirecting to Admin Dashboard ");
                return "redirect:/admin/dashboard";
            }else if(authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")){
                logger.info("Redirecting to Student Dashboard ");
                return "/admin_template/index";
            }
        }
        logger.info("Redirecting to Default user Dashboard ");
        return "/admin_template/index";
    }



    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied_GET(){
        logger.info("Access Denied page ");
        return "/templates/403";
    }
}
