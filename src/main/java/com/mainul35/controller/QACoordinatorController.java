package com.mainul35.controller;

import java.sql.Timestamp;
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

import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mainul35.config.SortTimelines;
import com.mainul35.config.Utils;
import com.mainul35.customModel.Timeline;
import com.mainul35.entity.Activity;
import com.mainul35.entity.Attachment;
import com.mainul35.entity.Comment;
import com.mainul35.entity.Contribution;
import com.mainul35.entity.Idea;
import com.mainul35.entity.Issue;
import com.mainul35.entity.Notification;
import com.mainul35.entity.Reaction;
import com.mainul35.entity.Tag;
import com.mainul35.entity.UserEntity;
import com.mainul35.service.ActivityService;
import com.mainul35.service.AttachmentService;
import com.mainul35.service.ContributionService;
import com.mainul35.service.IdeaService;
import com.mainul35.service.IssueService;
import com.mainul35.service.NotificationService;
import com.mainul35.service.TagService;
import com.mainul35.service.UserService;

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
	@Autowired
	private IssueService issueService;
	@Autowired
	private SortTimelines sortTimelines;
	
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
		if(!notificationId.isEmpty() && Character.isDigit(notificationId.charAt(0))) {
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
					t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

			model.addAttribute("categories", tags);

			model.addAttribute("ok", "false");
			model.addAttribute("msg", "Sorry, The category expired.");
			return "/qa_coordinator_template/post_an_idea";
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
					tags.removeIf(t -> t.getClosingDate() == null
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/submit-issue")
	public String raiseAnIssue_GET(Model model, HttpSession session) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("issue", new Issue());
		model.addAttribute("usr", userEntity);
		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(
				t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

		model.addAttribute("categories", tags);
		logger.info("Student -> Submit an issue : ");
		return "/qa_coordinator_template/submit_an_issue";
	}

	@RequestMapping(value = "/submit-issue", method = RequestMethod.POST)
	public String raiseAnIssue_POST(Model model, 
			HttpSession session, @ModelAttribute("issue") Issue issue,
			@RequestParam(name = "tagName", defaultValue = "") String tagName
			) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Tag tag = tagService.findByTagName(tagName);
		if (tag.getClosingDate().getTime() < System.currentTimeMillis()) {
			model.addAttribute("issue", new Issue());
			model.addAttribute("usr", userEntity);
			List<Tag> tags = tagService.listAllTags();
			tags.removeIf(
					t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

			model.addAttribute("categories", tags);
			model.addAttribute("ok", "false");
			model.addAttribute("msg", "Sorry, The category expired.");
			return "/qa_coordinator_template/submit_an_issue";
		}

		issue.setIssueId(System.currentTimeMillis());
		issue.setIssueRaisedDate(new Timestamp(System.currentTimeMillis()));
		issue.setIssueStatus("created");
		issue.setIssueSubmittedBy(userEntity);
		issue.setTag(tag);
		issue.setIssueUrl("/qa_manager/read-issue?issue_id="+issue.getIssueId());
		issue.setIssueSubmittedTo(userService.getUserByUsername("qa_manager"));
		
		issueService.save(issue);
		
		Activity activity = new Activity();
		activity.setId(userEntity.getId());
		activity.setLastActivityDateTime(new Timestamp(System.currentTimeMillis()));
		activityService.saveOrUpdate(activity);
		System.out.println(activity);
		logger.info("Student -> Submit issue [POST] : ");
		return "redirect:/qa_coordinator/dashboard";
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
		return "/qa_coordinator_template/read_issue";
	}

	@RequestMapping(value = "/all-issues", method = RequestMethod.GET)
	public String allIssues(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		model.addAttribute("issues", issueService.findByIssueSubmittedBy(userEntity));
		model.addAttribute("usr", userEntity);

		return "/qa_coordinator_template/list_all_issues";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/view-all-issues")
	public String viewAllIssues_GET(HttpSession session, Model model) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		return "/qa_coordinator_template/view_all_issues";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/most-liked-ideas")
	public @ResponseBody Timeline[] mostLikedIdeas_GET(HttpSession session) {
		
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		List<Idea> ideas = new ArrayList<>();
		List<Timeline> timelines = new ArrayList<>();
		ideaService.listAllIdeas().forEach(idea -> {
			Timeline timeline = new Timeline();
			UserEntity userEntity2 = userService.getUserByEmail(idea.getAuthorEmail());
			if(userEntity.getDepartment().contains(userEntity2.getDepartment())) {
				idea.getReactions().forEach(reaction->{
					if (reaction.getReactionType() == 1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
					} else if (reaction.getReactionType() == 2) {
						timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
					}
				});
				timeline.setIdea(idea);
				timeline.setPostedBy(userEntity2);
				timeline.setTotalComments(idea.getComments().size());
				timeline.setTotalSeenBy(idea.getSeenBy().size());
				if (!(idea.getTag().getFinalClosingDate().getTime() < new Date().getTime())) {
					timelines.add(timeline);
				}
			}
			});
		sortTimelines.SORT_TYPE = sortTimelines.SORT_BY_TOTAL_THUMB_UP;
		Timeline[] timelineArray = new Timeline[timelines.size()];
		System.out.println(timelineArray);
		timelineArray = sortTimelines.sort(timelines.toArray(timelineArray));
		ArrayUtils.reverse(timelineArray);
		return timelineArray;
	}

	@RequestMapping(method=RequestMethod.GET, value="/most-comented-ideas")
	public @ResponseBody Timeline[] mostCommentedIdeas_GET(HttpSession session) {
		
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		List<Idea> ideas = new ArrayList<>();
		List<Timeline> timelines = new ArrayList<>();
		ideaService.listAllIdeas().forEach(idea -> {
			Timeline timeline = new Timeline();
			UserEntity userEntity2 = userService.getUserByEmail(idea.getAuthorEmail());
			if(userEntity.getDepartment().contains(userEntity2.getDepartment())) {
				idea.getReactions().forEach(reaction->{
					if (reaction.getReactionType() == 1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
					} else if (reaction.getReactionType() == 2) {
						timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
					}
				});
				timeline.setIdea(idea);
				timeline.setPostedBy(userEntity2);
				timeline.setTotalComments(idea.getComments().size());
				timeline.setTotalSeenBy(idea.getSeenBy().size());
				if (!(idea.getTag().getFinalClosingDate().getTime() < new Date().getTime())) {
					timelines.add(timeline);
				}
			}
			});
		sortTimelines.SORT_TYPE = sortTimelines.SORT_BY_TOTAL_COMMENTS;
		Timeline[] timelineArray = new Timeline[timelines.size()];
		System.out.println(timelineArray);
		timelineArray = sortTimelines.sort(timelines.toArray(timelineArray));
		ArrayUtils.reverse(timelineArray);
		return timelineArray;
	}

	@RequestMapping(method=RequestMethod.GET, value="/most-visited-ideas")
	public @ResponseBody Timeline[] mostVisitedIdeas_GET(HttpSession session) {
		
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		List<Idea> ideas = new ArrayList<>();
		List<Timeline> timelines = new ArrayList<>();
		ideaService.listAllIdeas().forEach(idea -> {
			Timeline timeline = new Timeline();
			UserEntity userEntity2 = userService.getUserByEmail(idea.getAuthorEmail());
			if(userEntity.getDepartment().contains(userEntity2.getDepartment())) {
				idea.getReactions().forEach(reaction->{
					if (reaction.getReactionType() == 1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
					} else if (reaction.getReactionType() == 2) {
						timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
					}
				});
				timeline.setIdea(idea);
				timeline.setPostedBy(userEntity2);
				timeline.setTotalComments(idea.getComments().size());
				timeline.setTotalSeenBy(idea.getSeenBy().size());
				if (!(idea.getTag().getFinalClosingDate().getTime() < new Date().getTime())) {
					timelines.add(timeline);
				}
			}
			});
		sortTimelines.SORT_TYPE = sortTimelines.SORT_BY_TOTAL_VIEWS;
		Timeline[] timelineArray = new Timeline[timelines.size()];
		System.out.println(timelineArray);
		timelineArray = sortTimelines.sort(timelines.toArray(timelineArray));
		ArrayUtils.reverse(timelineArray);
		return timelineArray;
	}

	@RequestMapping(method=RequestMethod.GET, value="/most-seen-ideas")
	public @ResponseBody Timeline[] mostSeenIdeas_GET(HttpSession session) {
		
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		List<Idea> ideas = new ArrayList<>();
		List<Timeline> timelines = new ArrayList<>();
		ideaService.listAllIdeas().forEach(idea -> {
			Timeline timeline = new Timeline();
			UserEntity userEntity2 = userService.getUserByEmail(idea.getAuthorEmail());
			if(userEntity.getDepartment().contains(userEntity2.getDepartment())) {
				idea.getReactions().forEach(reaction->{
					if (reaction.getReactionType() == 1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp() + 1);
					} else if (reaction.getReactionType() == 2) {
						timeline.setTotalThumbDown(timeline.getTotalThumbDown() + 1);
					}
				});
				timeline.setIdea(idea);
				timeline.setPostedBy(userEntity2);
				timeline.setTotalComments(idea.getComments().size());
				timeline.setTotalSeenBy(idea.getSeenBy().size());
				if (!(idea.getTag().getFinalClosingDate().getTime() < new Date().getTime())) {
					timelines.add(timeline);
				}
			}
			});
		sortTimelines.SORT_TYPE = sortTimelines.SORT_BY_TOTAL_SEEN_BY;
		Timeline[] timelineArray = new Timeline[timelines.size()];
		System.out.println(timelineArray);
		timelineArray = sortTimelines.sort(timelines.toArray(timelineArray));
		ArrayUtils.reverse(timelineArray);
		return timelineArray;
	}

}
