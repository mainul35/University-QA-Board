package com.springprojects.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springprojects.config.Mailer;
import com.springprojects.config.Utils;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.DepartmentService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.NotificationService;
import com.springprojects.service.TagService;
import com.springprojects.service.UserService;

@Controller
@RequestMapping("/qa_manager")
public class QAManagerController {

	@Autowired
	private TagService tagService;
	@Autowired
	private Utils utils;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private IdeaService ideaService;
	@Autowired
	private UserService userService;
	@Autowired
	private ContributionService contributionService;
	@Autowired
	private ActivityService activityService;
	private int index = 0;
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(method = RequestMethod.GET, value = "/dashboard")
	public String qaManagerDashboard_GET(HttpSession session, Model model) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "/qa_manager_template/index";
	}

	/**
	 * 
	 * 
	 * Manage tags
	 * 
	 * 
	 */

	@RequestMapping(value = "/manage-tags", method = RequestMethod.GET)
	public String manageTag_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("tag", new Tag());
		List<String> departments = new ArrayList<>();
		departmentService.getAllDepartments().forEach(department -> {
			String departmentName = department.getDepartmentName();
			departments.add(departmentName);
		});
		model.addAttribute("departments", departments);
		return "/qa_manager_template/manage_tags";
	}

	@RequestMapping(value = "/create-tag", method = RequestMethod.POST)
	public String createTag_POST(Model model, HttpSession session, @ModelAttribute("tag") Tag tag,
			@RequestParam(name = "opening-date", required = true) String openingDate,
			@RequestParam(name = "closure-date", required = true) String closureDate,
			@RequestParam(name = "final-closure-date", required = true, defaultValue = "") String finalClosureDate,
			@RequestParam(name="inputVisibleToDepartments", required=true, defaultValue="") String departments) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);

		tag.setTagId(System.currentTimeMillis());
		tag.setOpeningDate(utils.convertStringToTimestamp(openingDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));
		tag.setClosingDate(utils.convertStringToTimestamp(closureDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));
		tag.setDepartments(departments);
		tag.setFinalClosingDate(utils.convertStringToTimestamp(finalClosureDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));

		System.out.println(tag.getOpeningDate() + "\t" + tag.getClosingDate() + "\t" + tag.getFinalClosingDate());
		if (tagService.save(tag) == false) {
			model.addAttribute("isOk", "false");
		} else {
			model.addAttribute("isOk", "true");
			InetAddress IP = null;
			try {
				IP = Inet4Address.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<UserEntity> userEntities = userService.getAllUsers();
			
			for (UserEntity userEntity2 : userEntities) {
				if(tag.getDepartments().contains(userEntity2.getDepartment())) {
					Mailer.sendMail(userEntity2.getEmail(), "EWSD - A new category has been opened.",
							"A new category has been opened. \n To post an idea, please click on the link below. \n http://ec2-18-220-231-146.us-east-2.compute.amazonaws.com:8080/ewsd/post-new-idea");

					Notification notification = new Notification();
					notification.setNotificationId(tag.getTagId());
					notification.setNotificationMsg("A new category has been opened.");
					notification.setNotificationType("announcement");
					notification.setNotificationUrl(
							"http://ec2-18-220-231-146.us-east-2.compute.amazonaws.com:8080/ewsd/post-new-idea");
					notification.setNotifyTo(userEntity2);
					notification.setNotificationFrom(userEntity);
					notification.setNotifiableDepartments(tag.getDepartments());
					notification.setSeen("no");

					notificationService.save(notification);
				}
			}
		}

		model.addAttribute("tag", tag);
		return "redirect:/qa_manager/manage-tags";
	}

	@RequestMapping(value = "/view-all-tags", method = RequestMethod.GET)
	public String viewAllTags_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "view-all-tags");
		List<Tag> tags = tagService.listAllTags();
		List<Integer> totalIdeasOfTag = new ArrayList<>();
		tags.forEach(tag->{
			totalIdeasOfTag.add(ideaService.listIdeasByTag(tag).size());
		});
		
		model.addAttribute("tags", tags);
		model.addAttribute("totalIdeas", totalIdeasOfTag);
		return "/qa_manager_template/view_all_tags";
	}
	
	@RequestMapping(value="/tag-status/{tagId}", method=RequestMethod.GET)
	public @ResponseBody Integer totalIdeasOfTag(@PathVariable("tagId") Long tagId){
		return ideaService.listIdeasByTag(tagService.findById(tagId)).size();
	}
	
	@RequestMapping(value="/delete-tag/{tagId}", method=RequestMethod.GET)
	public @ResponseBody boolean deleteTag(@PathVariable("tagId") Long tagId){
		notificationService.delete(notificationService.findById(tagId));
		return ideaService.listIdeasByTag(tagService.findById(tagId)).size()==0?tagService.delete(tagService.findById(tagId)):false;
	}
}
