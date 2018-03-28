package com.springprojects.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springprojects.config.Utils;
import com.springprojects.customModel.Timeline;
import com.springprojects.entity.Activity;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Contribution;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.NotificationService;
import com.springprojects.service.TagService;
import com.springprojects.service.UserService;

@Controller
@RequestMapping("/qa_coordinator")
public class QACoordinatorController {

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
	private int index = 0;
	private int countIdeas = 0;
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * 
	 * 			QA Coordinator - Dashboard
	 * 
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/dashboard")
	public String qaCoordinatorDashboard_GET(HttpSession session, Model model) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "redirect:/qa_coordinator/activities";
	}
	
	/**
	 * 
	 * 
	 * 			QA Coordinator - department activities
	 * 
	 * 
	 * */
	
	@RequestMapping(method = RequestMethod.GET, value = "/activities")
	public String departmentActivities_GET(HttpSession session, Model model) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "/qa_coordinator_template/activity";
	}
	
	/**
	 * 
	 * 
	 * 			QA Coordinator - Post new idea
	 * 
	 * 
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/post-new-idea")
	public String postNewIdea_GET(Model model, HttpSession session, @RequestParam(name="notif_id", defaultValue="") String notificationId) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		if(Character.isDigit(notificationId.charAt(0))) {
			Notification notification = notificationService.findById(Long.parseLong(notificationId));
			notification.setSeen("yes");
			notificationService.save(notification);
		}
		model.addAttribute("idea", new Idea());
		model.addAttribute("usr", userEntity);
		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(
				tag -> tag.getClosingDate() == null || tag.getClosingDate().getTime() < System.currentTimeMillis());

		model.addAttribute("categories", tags);
		logger.info("Student -> Post new idea : ");
		return "/qa_coordinator_template/post_an_idea";
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
			tags.removeIf(
					t -> tag.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

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
					return "/qa_coordinator_template/post_an_idea";
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
					return "/qa_coordinator_template/post_an_idea";
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
		return "redirect:/qa_coordinator/timeline";
	}
	
	
	/**
	 * 
	 * 
	 * 			QA Coordinator - Timeline
	 * 
	 * */
	
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

				List<Comment> comments = new ArrayList();

				idea.getComments().iterator().forEachRemaining(c1 -> {
					comments.add(index++, c1);
				});
				index = 0;
				idea.setComments(comments);
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

		return "/qa_coordinator_template/timeline";
	}

	/**
	 * 
	 * 
	 * 			QA Coordinator - my department ideas
	 * 
	 * */
	
	@RequestMapping(value = "/ideas/my-department", method = RequestMethod.GET)
	public String ideas_GET(HttpSession session, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		int resultPerPage = 5;
		// pageOfIdeas(pageNumber, resultPerPage)
		List<Idea> ideas = new ArrayList<>();
		index = 0;
		countIdeas = 0;
		ideaService.listAllIdeas().iterator().forEachRemaining(idea -> {
			if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment()
					.contains(userEntity.getDepartment())) {
				if (index >= (pageNumber - 1) * resultPerPage && index < pageNumber * resultPerPage) {
					ideas.add(idea);
				}
				index++;
				countIdeas++;
			}
		});

		index = 0;
		Collections.reverse(ideas);

		Map<String, List<Timeline>> dates = new TreeMap<>(Comparator.reverseOrder());

		String dateTimeString = "";

		for (Idea idea : ideas) {

			dateTimeString = utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa");
			String time = dateTimeString.split(" ")[1] + " " + dateTimeString.split(" ")[2];

			if (!dates.containsKey(dateTimeString.split(" ")[0])) {
				dates.put(dateTimeString.split(" ")[0], new ArrayList());
			}
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

				List<Comment> comments = new ArrayList();

				idea.getComments().iterator().forEachRemaining(c1 -> {
					comments.add(index++, c1);
				});
				index = 0;
				idea.setComments(comments);
				timeline.setTotalComments(idea.getComments().size());

				dates.get(dateTimeString.split(" ")[0]).add(timeline);

			}
		}
		int totalResults = ideas.size();

		int pages = (int) Math.ceil((double) countIdeas / resultPerPage);

		model.addAttribute("pages", totalResults == 5 ? pages - 1 : resultPerPage == totalResults ? 0 : pages - 1);

		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);
		logger.info("templates -> ideas : ");

		return "/qa_coordinator_template/my_department_ideas";
	}

	
	/**
	 * 
	 * 
	 * 			QA Coordinator - Staff ideas
	 * 
	 * */
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	@RequestMapping(value = "/ideas/staff-ideas", method = RequestMethod.GET)
	public String ideasOfOthers_GET(HttpSession session, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		int resultPerPage = 5;
		// pageOfIdeas(pageNumber, resultPerPage)
		List<Idea> ideas = new ArrayList<>();
		index = 0;
		countIdeas = 0;
		ideaService.listAllIdeas().iterator().forEachRemaining(idea -> {
			userService.getUserByEmail(idea.getAuthorEmail()).getAuthorities().forEach(authority -> {
				if (authority.getAuthority().equalsIgnoreCase("ROLE_STAFF")) {
					if (index >= (pageNumber - 1) * resultPerPage && index < pageNumber * resultPerPage) {
						ideas.add(idea);
					}
					index++;
					countIdeas++;
				}
			});
		});

		Collections.reverse(ideas);
		index = 0;
		Map<String, List<Timeline>> dates = new TreeMap<>(Comparator.reverseOrder());

		String dateTimeString = "";
		

		for (Idea idea : ideas) {

			dateTimeString = utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa");
			String time = dateTimeString.split(" ")[1] + " " + dateTimeString.split(" ")[2];

			if (!dates.containsKey(dateTimeString.split(" ")[0])) {
				dates.put(dateTimeString.split(" ")[0], new ArrayList());
			}
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

				List<Comment> comments = new ArrayList<Comment>();

				idea.getComments().iterator().forEachRemaining(c1 -> {
					comments.add(index++, c1);
				});
				index = 0;
				idea.setComments(comments);
				timeline.setTotalComments(idea.getComments().size());

				dates.get(dateTimeString.split(" ")[0]).add(timeline);

			}
		}
		int totalResults = ideas.size();

		int pages = (int) Math.ceil((double) countIdeas / resultPerPage);

		model.addAttribute("pages", totalResults == 5 ? pages - 1 : resultPerPage == totalResults ? 0 : pages - 1);

		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);
		logger.info("templates -> ideas : ");

		return "/qa_coordinator_template/other_department_ideas";
	}

	/**
	 * 
	 * 
	 * 			QA Coordinator - Raise an issue
	 * 
	 * */
	
	@RequestMapping(method = RequestMethod.GET, value = "/raise-an-issue")
	public String raiseAnIssue_GET(Model model, HttpSession session) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("idea", new Idea());
		model.addAttribute("usr", userEntity);
		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(
				tag -> tag.getClosingDate() == null || tag.getClosingDate().getTime() < System.currentTimeMillis());

		model.addAttribute("categories", tags);
		logger.info("Student -> Post new idea : ");
		return "/qa_coordinator_template/post_an_idea";
	}

	@RequestMapping(value = "/raise-an-issue", method = RequestMethod.POST)
	public String raiseAnIssue_POST(Model model, HttpSession session, @ModelAttribute("idea") Idea idea,
			@RequestParam(name = "tagName", defaultValue = "") String tagName,
			@RequestParam(name = "publishingDateTime") String publishingDateTime,
			@RequestParam(name = "images[]") MultipartFile[] files) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Tag tag = tagService.findByTagName(tagName);
		if (tag.getClosingDate().getTime() < System.currentTimeMillis()) {
			model.addAttribute("idea", new Idea());
			model.addAttribute("usr", userEntity);
			List<Tag> tags = tagService.listAllTags();
			tags.removeIf(
					t -> tag.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

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
					return "/qa_coordinator_template/post_an_idea";
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
					return "/qa_coordinator_template/post_an_idea";
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
		return "redirect:/qa_coordinator/timeline";
	}

}
