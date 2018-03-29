package com.springprojects.controller;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.set.MapBackedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springprojects.config.Mailer;
import com.springprojects.config.Utils;
import com.springprojects.customModel.Timeline;
import com.springprojects.entity.Activity;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Contribution;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.CommentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.NotificationService;
import com.springprojects.service.TagService;
import com.springprojects.service.UserService;

@Controller
@RequestMapping("/student")
public class StudentController {

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
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private AuthorityService authorityService;
	private int index = 0;
	private Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(method = RequestMethod.GET, value = "/dashboard")
	public String studentDashboard_GET() {
		return "redirect:/student/timeline";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/post-new-idea")
	public String postNewIdea_GET(Model model, HttpSession session, @RequestParam(name="notif_id") String notificationId) {

		if(!notificationId.isEmpty() && Character.isDigit(notificationId.charAt(0))) {
			Notification notification = notificationService.findById(Long.parseLong(notificationId));
			notification.setSeen("yes");
			notificationService.save(notification);
		}
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("idea", new Idea());
		model.addAttribute("usr", userEntity);
		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(tag -> tag.getClosingDate() == null || tag.getClosingDate().getTime() < System.currentTimeMillis()
				|| !tag.getDepartments().contains(userEntity.getDepartment()));

		model.addAttribute("categories", tags);
		logger.info("Student -> Post new idea : ");
		return "/student_template/post_an_idea";
	}

	@RequestMapping(value = "/post-new-idea", method = RequestMethod.POST)
	public String postNewIdea_POST(Model model, HttpSession session, @ModelAttribute("idea") Idea idea,
			@RequestParam(name = "tagName", defaultValue = "") String tagName,
			@RequestParam(name = "publishingDateTime") String publishingDateTime,
			@RequestParam(name = "images[]") MultipartFile[] files) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Tag tag = tagService.findByTagName(tagName);
		if (tag.getClosingDate().getTime() < System.currentTimeMillis()) {
			model.addAttribute("idea", new Idea());
			model.addAttribute("usr", userEntity);
			List<Tag> tags = tagService.listAllTags();
			tags.removeIf(t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis()
					|| !t.getDepartments().contains(userEntity.getDepartment()));

			model.addAttribute("categories", tags);

			model.addAttribute("ok", "false");
			model.addAttribute("msg", "Sorry, The category expired.");
			return "/student_template/post_an_idea";
		}
		idea.setTag(tagService.findByTagName(tagName));
		idea.setAuthorEmail(userEntity.getEmail());
		idea.setCountViews(0);
		idea.setIdeaId(System.currentTimeMillis());
		idea.setPublishingDate(utils.convertStringToTimestamp(publishingDateTime, "dd-MM-yyyy HH:mm:ss"));

		InetAddress IP = null;
		try {
			IP = Inet4Address.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Notification notification = new Notification();
		notification.setNotificationId(idea.getIdeaId());
		notification.setNotifiableDepartments(userEntity.getDepartment());
		notification.setNotificationFrom(userEntity);
		notification.setNotificationType("notification");
		notification.setNotificationMsg("EWSD - A new idea is submitted from your department.");
		notification.setNotificationUrl("/ewsd/ideas/" + idea.getIdeaId());
		notification.setSeen("no");
		userService.getUsersByDepartment(userEntity.getDepartment()).forEach(user -> {
			for (Authority authority : user.getAuthorities()) {
				if (authority.getAuthority().equalsIgnoreCase("ROLE_QA_COORDINATOR")) {
					notification.setNotifyTo(user);
				}
			}
		});

		notificationService.save(notification);

		Mailer.sendMail(notification.getNotifyTo().getEmail(), "EWSD - A new idea is submitted from your department.",
				"To read the comment, please click on the link below. \n http://ec2-13-127-223-157.ap-south-1.compute.amazonaws.com:8080/ewsd/ideas/"
						+ idea.getIdeaId());

		Set<Attachment> attachments = new HashSet<>();
		if (files[0].getOriginalFilename().contains(".")) {
			for (MultipartFile file : files) {
				if (file.getSize() > 25000000L) {
					model.addAttribute("idea", new Idea());
					model.addAttribute("usr", userEntity);
					List<Tag> tags = tagService.listAllTags();
					tags.removeIf(t -> tag.getClosingDate() == null
							|| t.getClosingDate().getTime() < System.currentTimeMillis());

					model.addAttribute("categories", tags);

					model.addAttribute("ok", "false");
					model.addAttribute("msg", "Sorry, The file size is too large.");
					return "/student_template/post_an_idea";
				}
				System.out.println(file.getContentType());
				if (!file.getContentType().contains("image") && !file.getOriginalFilename().contains(".doc")
						&& !file.getOriginalFilename().contains(".docx") && !file.getContentType().contains("video")
						&& !file.getOriginalFilename().contains(".pdf")) {
					System.out.println(file.getOriginalFilename());
					model.addAttribute("idea", new Idea());
					model.addAttribute("usr", userEntity);
					List<Tag> tags = tagService.listAllTags();
					tags.removeIf(t -> tag.getClosingDate() == null
							|| t.getClosingDate().getTime() < System.currentTimeMillis());

					model.addAttribute("categories", tags);

					model.addAttribute("ok", "false");
					model.addAttribute("msg", "Sorry, Unknown file type found.");
					return "/student_template/post_an_idea";
				}
				Attachment attachment = new Attachment();
				Long attachmentId = System.currentTimeMillis();
				attachment.setAttachmentId(attachmentId);
				attachment.setFileName("" + attachmentId);
				attachment.setFileTitle(idea.getIdeaTitle());

				attachment = attachmentService.save(attachment, file, userEntity.getId());

				attachments.add(attachment);
			}

		}

		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

		idea.setAttachments(attachments);
		ideaService.save(idea);

		Contribution contribution = new Contribution(idea.getIdeaId(), userEntity);
		contributionService.save(contribution);
		Activity activity = new Activity();
		activity.setId(userEntity.getId());
		activity.setLastActivityDateTime(utils.convertStringToTimestamp(publishingDateTime, "dd-MM-yyyy HH:mm:ss"));
		activityService.saveOrUpdate(activity);
		logger.info("Student -> Post new idea : ");
		return "redirect:/student/timeline";
	}

	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public String timeline_GET(HttpSession session, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		int resultPerPage = 5;

		List<Idea> ideas = new ArrayList<>();
		ideaService.listAllIdeasByAuthorEmail(userEntity.getEmail(), pageNumber, resultPerPage).iterator()
				.forEachRemaining(idea -> {
					ideas.add(idea);
				});

		// Collections.reverse(ideas);
		Map<String, List<Timeline>> dates = new TreeMap<>(Comparator.reverseOrder());

		String dateTimeString = "";

		for (Idea idea : ideas) {

			dateTimeString = utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa");
			String time = dateTimeString.split(" ")[1] + " " + dateTimeString.split(" ")[2];

			if (!dates.containsKey(dateTimeString.split(" ")[0])) {
				dates.put(dateTimeString.split(" ")[0], new ArrayList());
			}

			List<Comment> comments = new ArrayList<>();

			for (Comment comment : idea.getComments()) {
				for (Authority authority : comment.getCommentedUser().getAuthorities()) {
					if(authority.getAuthority().equals("ROLE_STUDENT")) {
						comments.add(comment);
						break;
					}
				}
			}
			
			idea.setComments(comments);
			if (dates.containsKey(dateTimeString.split(" ")[0])) {
				Timeline timeline = new Timeline(time, idea);
				timeline.setPostedBy(userService.getUserByEmail(idea.getAuthorEmail()));
				for (Reaction reaction : idea.getReactions()) {
					if (reaction.getReactionType() == 1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
					} else if (reaction.getReactionType() == 2) {
						timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
					}
					if (reaction.getReactedUser().getUsername().equals(userEntity.getUsername())) {
						timeline.setReactionOfCurrentUser(reaction.getReactionType());
					}
				}

				if (idea.getTag().getFinalClosingDate().getTime() < new Date().getTime()) {
					timeline.setTagExpired(true);
				}

				timeline.setTotalComments(idea.getComments().size());

				dates.get(dateTimeString.split(" ")[0]).add(timeline);

			}

		}

		int totalResults = ideas.size();

		// profile side panel items

		int pages = (int) Math
				.ceil(((double) ideaService.listAllIdeasByAuthorEmail(userEntity.getEmail()).size()) / resultPerPage);
		List<Idea> ideas2 = ideaService.listAllIdeasByAuthorEmail(userEntity.getEmail());
		Set<Tag> tags = new HashSet<>();

		ideas2.forEach((idea) -> tags.add(idea.getTag()));

		model.addAttribute("totalIdeas", ideas2.size());
		model.addAttribute("totalTags", tags.size());
		model.addAttribute("totalContributions", contributionService.findContributionsByUser(userEntity).size());

		// timeline data

		model.addAttribute("pages", totalResults == 5 ? pages - 1 : resultPerPage == totalResults ? 0 : pages - 1);

		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);

		logger.info("Student -> timeline : ");

		return "/student_template/timeline";
	}

	// @RequestMapping(value = "/ideas", method = RequestMethod.GET)
	// public String ideas_GET(HttpSession session, Model model,
	// @RequestParam(name = "page", defaultValue = "1") int pageNumber) {
	// UserEntity userEntity = (UserEntity) session.getAttribute("usr");
	//
	// int resultPerPage = 5;
	//
	// List<Idea> ideas = new ArrayList<>();
	// ideaService.getPageOfIdeas(pageNumber,
	// resultPerPage).iterator().forEachRemaining(idea -> {
	// ideas.add(idea);
	// });
	//
	// Collections.reverse(ideas);
	//
	// Map<String, List<Timeline>> dates = new TreeMap<>(Comparator.reverseOrder());
	//
	// String dateTimeString = "";
	//
	// for (Idea idea : ideas) {
	//
	// dateTimeString = utils.convertTimestampToString(idea.getPublishingDate(),
	// "d/MM/YYYY hh:mm:ss aaa");
	// String time = dateTimeString.split(" ")[1] + " " + dateTimeString.split("
	// ")[2];
	//
	// if (!dates.containsKey(dateTimeString.split(" ")[0])) {
	// dates.put(dateTimeString.split(" ")[0], new ArrayList());
	// }
	// if (dates.containsKey(dateTimeString.split(" ")[0])) {
	// Timeline timeline = new Timeline(time, idea);
	// timeline.setPostedBy(userService.getUserByEmail(idea.getAuthorEmail()));
	// for (Reaction reaction : idea.getReactions()) {
	// if (reaction.getReactionType() == 1) {
	// timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
	// } else if (reaction.getReactionType() == 2) {
	// timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
	// }
	// if (reaction.getReactedUser().getUsername().equals(userEntity.getUsername()))
	// {
	// timeline.setReactionOfCurrentUser(reaction.getReactionType());
	// }
	// }
	//
	// if (idea.getTag().getFinalClosingDate().getTime() < new Date().getTime()) {
	// timeline.setTagExpired(true);
	// }
	//
	// List<Comment> comments = new ArrayList();
	//
	// idea.getComments().iterator().forEachRemaining(c1->{
	// comments.add(index++, c1);
	// });
	// index = 0;
	// idea.setComments(comments);
	// timeline.setTotalComments(idea.getComments().size());
	//
	// dates.get(dateTimeString.split(" ")[0]).add(timeline);
	//
	// Collections.reverse(dates.get(dateTimeString.split(" ")[0]));
	// Collections.reverse(dates.get(dateTimeString.split(" ")[0]));
	//
	// }
	// }
	// int totalResults = ideas.size();
	// int pages = (int) Math.ceil(((double) totalResults) / resultPerPage);
	// model.addAttribute("pages",
	// resultPerPage == 5 ? ideaService.count(userEntity.getEmail(), pageNumber,
	// resultPerPage) - 1
	// : resultPerPage == totalResults ? 0 : pages - 1);
	// model.addAttribute("currentPage", pageNumber);
	//
	// model.addAttribute("usr", userEntity);
	// model.addAttribute("dates", dates);
	// model.addAttribute("utils", utils);
	// logger.info("templates -> ideas : ");
	//
	// return "/templates/ideas";
	// }

}
