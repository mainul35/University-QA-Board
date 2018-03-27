package com.springprojects.controller;

import com.springprojects.config.Mailer;
import com.springprojects.config.Utils;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Batch;
import com.springprojects.entity.Department;
import com.springprojects.entity.Notification;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.BatchService;
import com.springprojects.service.DepartmentService;
import com.springprojects.service.NotificationService;
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
	private UserService userService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private TagService tagService;
	@Autowired
	private Utils utils;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private BatchService batchService;
	private Logger logger = Logger.getLogger(AdminController.class.getName());
	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		logger.info("Admin Dashboard : ");
		return "/admin_template/index";
	}

	@RequestMapping(value = "/set-terms-and-conditions", method = RequestMethod.GET)
	public String setTermsAndConditions_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		String tNc = utils.readFile("/TermsAndConditions.txt");
		model.addAttribute("tNc", tNc);
		model.addAttribute("pageName", "terms-and-conditions");
		return "/admin_template/index";
	}

	@RequestMapping(value = "/set-terms-and-conditions", method = RequestMethod.POST)
	public String setTermsAndConditions_POST(Model model, HttpSession session, @RequestParam("editor1") String tc) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "terms-and-conditions");
		tc = utils.writeFile("/TermsAndConditions.txt", tc);
		model.addAttribute("tNc", tc);
		return "/admin_template/index";
	}

	/**
	 * 
	 * 
	 * Manage Role
	 * 
	 * 
	 */
	@RequestMapping(value = "/create-role", method = RequestMethod.GET)
	public String createRole_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "create-role");
		Authority authority = new Authority();
		model.addAttribute("role", authority);
		return "/admin_template/index";
	}

	@RequestMapping(value = "/create-role", method = RequestMethod.POST)
	public String createRole_POST(Model model, HttpSession session, @ModelAttribute("role") Authority role) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "create-role");

		role.setId(System.currentTimeMillis());
		if (authorityService.create(role) == null) {
			model.addAttribute("isOk", "false");
		} else {
			model.addAttribute("isOk", "true");
		}
		model.addAttribute("role", role);
		return "/admin_template/index";
	}

	@RequestMapping(value = "/view-all-roles", method = RequestMethod.GET)
	public String viewAllRoles_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "view-all-roles");
		model.addAttribute("roles", authorityService.listAllAuthorities());
		return "/admin_template/index";
	}

	/**
	 * 
	 * 
	 * Manage Batch
	 * 
	 * 
	 */

	@RequestMapping(value = "/manage-batch", method = RequestMethod.GET)
	public String manageBatch_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "/admin_template/manage_batch";
	}
	
	@RequestMapping(value = "/create-batch", method = RequestMethod.GET)
	public String createBatch_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		Batch batch = new Batch();
		model.addAttribute("batch", batch);
		return "/admin_template/create_batch";
	}

	@RequestMapping(value = "/create-batch", method = RequestMethod.POST)
	public String createBatch_POST(Model model, HttpSession session, @ModelAttribute("batch") Batch batch) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		batch.setBatchId(System.currentTimeMillis());
		batchService.save(batch);
		
		model.addAttribute("batch", batch);
		return "/admin_template/manage_batch";
	}
	
	@RequestMapping(value = "/view-batches", method = RequestMethod.GET)
	public String viewBatches_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("msg", "");
		model.addAttribute("usr", userEntity);
		model.addAttribute("batches", batchService.findAllBatches());
		logger.info("Admin -> view-batches[GET] : ");

		return "/admin_template/view_all_batches";
	}

	
	/**
	 * 
	 * 
	 * Manage Department
	 * 
	 * 
	 */

	@RequestMapping(value = "/manage-department", method = RequestMethod.GET)
	public String manageDepartment_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "/admin_template/manage_department";
	}
	
	@RequestMapping(value = "/create-department", method = RequestMethod.GET)
	public String createDepartment_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		Department department = new Department();
		model.addAttribute("department", department);
		model.addAttribute("batches", batchService.findAllBatches());
		
		return "/admin_template/create_department";
	}

	@RequestMapping(value = "/create-department", method = RequestMethod.POST)
	public String createDepartment_POST(Model model, HttpSession session, @ModelAttribute("department") Department department) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		department.setDepartmentId(System.currentTimeMillis());
		String status = departmentService.saveOrUpdate(department);

		Notification notification = new Notification();
		notification.setNotificationId(department.getDepartmentId());
		notification.setNotificationMsg("EWSD - A new department has been added.");
		notification.setNotifiableDepartments("QA");
		notification.setNotificationFrom(userEntity);
		notification.setNotificationType("notification");
		notification.setNotificationUrl("#");
		notification.setNotifyTo(userService.getUserByUsername("qa_manager"));
		notification.setSeen("no");
		notificationService.save(notification);		
		
		model.addAttribute("department", department);
		return "/admin_template/manage_department";
	}
	
	@RequestMapping(value = "/view-departments", method = RequestMethod.GET)
	public String viewDestinations_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("msg", "");
		model.addAttribute("usr", userEntity);
		model.addAttribute("departments", departmentService.getAllDepartments());
		logger.info("Admin -> view-departments[GET] : ");

		return "/admin_template/view_all_departments";
	}
}
