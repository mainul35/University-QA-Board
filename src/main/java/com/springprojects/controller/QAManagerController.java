package com.springprojects.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
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
import com.springprojects.entity.Issue;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.DepartmentService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.IssueService;
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
	@Autowired
	private IssueService issueService;

	private String userType;

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
	public String manageTag_GET(Model model, HttpSession session,
			@RequestParam(name = "notif_id", defaultValue = "") String notificationId) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("tag", new Tag());

		if (!notificationId.isEmpty() && Character.isDigit(notificationId.charAt(0))) {
			Notification notification = notificationService.findById(Long.parseLong(notificationId));
			notification.setSeen("yes");
			notificationService.save(notification);
		}

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
			@RequestParam(name = "inputVisibleToDepartments", required = true, defaultValue = "") String departments) {

		if(tag.getTagName().isEmpty() || departments.isEmpty()) {
			return "redirect:/qa_manager/manage-tags";
		}
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		if (tagService.findByTagName(tag.getTagName()) == null) {
			tag.setTagId(System.currentTimeMillis());
			tag.setOpeningDate(utils.convertStringToTimestamp(openingDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));
			tag.setClosingDate(utils.convertStringToTimestamp(closureDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));
			tag.setDepartments(departments);
			tag.setFinalClosingDate(
					utils.convertStringToTimestamp(finalClosureDate + " 00:00:00", "dd/MM/yyyy HH:mm:ss"));
			tagService.save(tag);
			System.out.println(tag.getOpeningDate() + "\t" + tag.getClosingDate() + "\t" + tag.getFinalClosingDate());
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
				if (tag.getDepartments().contains(userEntity2.getDepartment())) {
					userEntity2.getAuthorities().forEach(authority -> {
						if (authority.getAuthority().equals("ROLE_STUDENT")) {
							userType = "student";
						} else if (authority.getAuthority().equals("ROLE_QA_COORDINATOR")) {
							userType = "qa_coordinator";
						}
					});
					Mailer.sendMail(userEntity2.getEmail(), "EWSD - A new category has been opened.",
							"A new category has been opened.");
					Notification notification = new Notification();
					notification.setNotificationId(System.currentTimeMillis());
					notification.setNotificationMsg("A new category has been opened.");
					notification.setNotificationType("announcement");
					notification.setNotificationUrl("/" + userType + "/post-new-idea");
					notification.setNotifyTo(userEntity2);
					notification.setNotificationFrom(userEntity);
					notification.setNotifiableDepartments(tag.getDepartments());
					notification.setSeen("no");

					notificationService.save(notification);
				}
			}

			model.addAttribute("tag", tag);
			return "redirect:/qa_manager/manage-tags";
		} else {
			model.addAttribute("isOk", "false");
			model.addAttribute("tag", new Tag());
			return "redirect:/qa_manager/manage-tags";
		}

	}

	@RequestMapping(value = "/view-all-tags", method = RequestMethod.GET)
	public String viewAllTags_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		model.addAttribute("pageName", "view-all-tags");
		List<Tag> tags = tagService.listAllTags();
		List<Integer> totalIdeasOfTag = new ArrayList<>();
		tags.forEach(tag -> {
			totalIdeasOfTag.add(ideaService.listIdeasByTag(tag).size());
		});

		model.addAttribute("tags", tags);
		model.addAttribute("totalIdeas", totalIdeasOfTag);
		return "/qa_manager_template/view_all_tags";
	}

	@RequestMapping(value = "/tag-status/{tagId}", method = RequestMethod.GET)
	public @ResponseBody Integer totalIdeasOfTag(@PathVariable("tagId") Long tagId) {
		return ideaService.listIdeasByTag(tagService.findById(tagId)).size();
	}

	@RequestMapping(value = "/delete-tag/{tagId}", method = RequestMethod.GET)
	public @ResponseBody boolean deleteTag(@PathVariable("tagId") Long tagId) {
		Notification notification = notificationService.findById(tagId);
		if (notification != null) {
			notificationService.delete(notification);
		}
		return ideaService.listIdeasByTag(tagService.findById(tagId)).size() == 0
				? tagService.delete(tagService.findById(tagId))
				: false;
	}

	@RequestMapping(value = "/read-issue", method = RequestMethod.GET)
	public String readIssue(Model model, HttpSession session,
			@RequestParam(name = "issue_id", defaultValue = "") String issueId) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		if (!issueId.isEmpty() && Character.isDigit(issueId.charAt(0))) {
			Issue issue = issueService.findById(Long.parseLong(issueId));
			issue.setIssueStatus("Processing");
			issueService.save(issue);
			model.addAttribute("issue", issue);
			model.addAttribute("usr", userEntity);
		}
		return "/qa_manager_template/read_issue";
	}

	@RequestMapping(value = "/all-issues", method = RequestMethod.GET)
	public String allIssues(Model model, HttpSession session,
			@RequestParam(name = "issue_id", defaultValue = "") String issueId,
			@RequestParam(name = "action_type", defaultValue = "") String actionType) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		if (!issueId.isEmpty() && Character.isDigit(issueId.charAt(0))) {
			Issue issue = issueService.findById(Long.parseLong(issueId));
			if (actionType.equals("close")) {
				issue.setIssueClosureDate(new Timestamp(System.currentTimeMillis()));
				issue.setIssueStatus("Closed");
			} else if (actionType.equals("solve")) {
				issue.setIssueClosureDate(new Timestamp(System.currentTimeMillis()));
				issue.setIssueStatus("Solved");
			}

			issueService.save(issue);

			return "redirect:/qa_manager/dashboard";
		}
		model.addAttribute("issues", issueService.findAllIssues());
		model.addAttribute("usr", userEntity);

		return "/qa_manager_template/view_all_issues";
	}

}
