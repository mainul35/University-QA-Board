package com.springprojects.controller;

import com.springprojects.config.Initializer;
import com.springprojects.config.Properties;
import com.springprojects.config.Utils;
import com.springprojects.customModel.Timeline;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Issue;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.IssueRepository;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.CommentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.DepartmentService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.IssueService;
import com.springprojects.service.NotificationService;
import com.springprojects.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private IdeaService ideaService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private Utils utils;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ContributionService contributionService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private IssueService issueService;
	@Autowired
	private ServletContext servletContext;
	private int index = 0;
	private int countIdeas = 0;

	// @RequestMapping(value = {"/"})
	// public String index(HttpServletRequest request){
	// System.out.println("Remote IP = "+request.getRemoteAddr());
	// System.out.println("Remote Host = "+request.getRemoteHost());
	// System.out.println("Remote Port = "+request.getRemotePort());
	// System.out.println("Remote User = "+request.getRemoteUser());
	// return "index";
	// }

	private static final Logger logger = Logger.getLogger(UserController.class.getName());

	public void log(HttpServletRequest request) {
		logger.info("Remote IP = " + request.getRemoteAddr());
		logger.info("Remote Host = " + request.getRemoteHost());
		logger.info("Remote Port = " + request.getRemotePort());
		logger.info("Remote User = " + request.getLocale().getDisplayCountry());
	}

	@RequestMapping("/")
	public String index(HttpServletRequest request, SitePreference sitePreference, Model model, Principal principal) {
		if (principal != null) {
			return "redirect:/dashboard";
		}
		if (sitePreference == SitePreference.NORMAL) {
			log(request);
			model.addAttribute("msg", "some message");
//			Initializer initializer = new Initializer(authorityService, userService, attachmentService,
//					departmentService, encoder);
			Properties.TEMP_PATH = servletContext.getRealPath("\\");
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
	public String login_GET(Principal principal) {
		if (principal != null) {
			return "redirect:/dashboard";
		}
		logger.info("Login screen : ");
		return "/templates/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login_POST() {
		return "/templates/login";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signUp_GET(Model model, Principal principal) {
		if (principal != null) {
			return "redirect:/dashboard";
		}
		UserEntity user = new UserEntity();
		model.addAttribute("id", System.currentTimeMillis());
		model.addAttribute("user", user);
		logger.info("Sign up page ");
		return "/templates/signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signUp_POST(@ModelAttribute("user") UserEntity user, @RequestParam("id") Long id,
			@RequestParam(name = "asTeacher", defaultValue = "false") Boolean asTeacher, Model model) {
		user.setId(id);
		userService.createUser(user, asTeacher);
		userService.loadUserByUsername("mainuls18");
		logger.info("Attempting to login : ");
		return "/templates/signup";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard_GET(HttpSession session, Model model) {
		model.addAttribute("username", session.getAttribute("username").toString());
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				logger.info("Redirecting to Admin Dashboard ");
				return "redirect:/admin/dashboard";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				logger.info("Redirecting to Student Dashboard ");
				return "redirect:/student/dashboard";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_MANAGER")) {
				logger.info("Redirecting to QA Manager Dashboard ");
				return "redirect:/qa_manager/dashboard";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_COORDINATOR")) {
				logger.info("Redirecting to QA Coordinator Dashboard ");
				return "redirect:/qa_coordinator/dashboard";
			}
		}
		logger.info("Redirecting to Default user Dashboard ");
		return "/admin_template/index";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied_GET() {
		logger.info("Access Denied page ");
		return "/templates/403";
	}

	@RequestMapping(value = "/404", method = RequestMethod.GET)
	public String notFound_GET() {
		logger.info("Access Denied page ");
		return "/templates/404";
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.GET)
	public String resetPassword_GET(Model model) {
		model.addAttribute("ok", "");
		return "/templates/reset_password";
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public String resetPassword_POST(Model model, @RequestParam("email") String email) {
		if (userService.existsWithEmail(email)) {
			userService.sendPasswordResetLink(email);
			model.addAttribute("ok", "true");
		} else {
			model.addAttribute("ok", "invalid-email");
		}
		return "/templates/reset_password";
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	public String updatePassword_GET(Model model, @RequestParam("id") String username,
			@RequestParam("adp") Long timestamp) {
		long currentMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
		long minutesWhenRequested = TimeUnit.MILLISECONDS.toMinutes(timestamp);
		System.out.println(currentMinutes - minutesWhenRequested);
		if (currentMinutes - minutesWhenRequested > 2) {
			model.addAttribute("ok", "false");
			return "/templates/reset_password";
		}
		model.addAttribute("id", username);
		model.addAttribute("ok", "");
		return "/templates/update_password";
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	public String updatePassword_POST(Model model, @RequestParam("id") String username,
			@RequestParam("password") String password, @RequestParam("re_password") String rePassword) {
		UserEntity userEntity = userService.getUserByUsername(username);
		if (password.equals(rePassword)) {
			userEntity.setPassword(encoder.encode(password));
			System.out.println(password);
			userService.updateUser(userEntity);
			model.addAttribute("ok", "true");
		} else {
			model.addAttribute("ok", "false");
		}

		return "/templates/update_password";
	}

	@RequestMapping(value = "/main-header", method = RequestMethod.GET)
	public String mainHeader_GET(Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		List<Notification> notifications = new ArrayList<>();
		int x = 0;
		String userType = "";
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				userType = "admin";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				userType = "student";
				List<Notification> notifications2 = notificationService.findNotificationsByUser(userEntity);
				for (Notification notification : notifications2) {
					if (notification.getNotificationType().equalsIgnoreCase("notification")) {
						for (GrantedAuthority authority2 : notification.getNotificationFrom().getAuthorities()) {
							if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")
									&& !notification.getNotificationFrom().equals(notification.getNotifyTo())) {
								notifications.add(notification);
								x++;
							}
						}
					}
					if (x > 10) {
						break;
					}
				}

			}

			if (authority.getAuthority().toUpperCase().equals("ROLE_STAFF")) {
				userType = "staff";
				List<Notification> notifications2 = notificationService.findNotificationsByUser(userEntity);
				for (Notification notification : notifications2) {
					if (notification.getNotificationType().equalsIgnoreCase("notification")
							&& !notification.getNotificationFrom().equals(notification.getNotifyTo())) {
						notifications.add(notification);
						x++;
					}
					if (x > 10) {
						break;
					}
				}
			}
		}

		List<Notification> announcements = new ArrayList<>();
		x = 0;
		for (Notification notification : notificationService.findNotificationsByUser(userEntity)) {
			if (notification.getNotificationType().equalsIgnoreCase("announcement")) {
				announcements.add(notification);
				x++;
			}

			if (x > 10) {
				break;
			}
		}

		int unreadNotifications = 0;
		int unreadAnnouncements = 0;
		
		for (Notification notification : notificationService.findNotificationsByUser(userEntity)) {
			if (notification.getNotifiableDepartments() != null
					&& notification.getNotifiableDepartments().contains(userEntity.getDepartment())) {
				if (notification.getNotificationType().equalsIgnoreCase("notification")
						&& notification.getSeen().equalsIgnoreCase("no")
						&& !notification.getNotificationFrom().equals(notification.getNotifyTo())) {
					unreadNotifications++;
				}

				if (notification.getNotificationType().equalsIgnoreCase("announcement")
						&& notification.getSeen().equalsIgnoreCase("no")
						&& !notification.getNotificationFrom().equals(notification.getNotifyTo())) {
					unreadAnnouncements++;
				}
			}
		}
		
		List<Issue> issues = new ArrayList<>();
		if(issueService.findAllIssues()!=null && issueService.findAllIssues().size()>0) {
			issueService.findAllIssues().forEach(issue->{
				if(issue.getIssueStatus().equals("created")) {
					issues.add(issue);
				}
			});
		}
		
		
		model.addAttribute("notifications", notifications);
		model.addAttribute("unreadNotifications", unreadNotifications);
		model.addAttribute("unreadAnnouncements", unreadAnnouncements);
		model.addAttribute("announcements", announcements);
		model.addAttribute("createdIssues", issues.size());
		model.addAttribute("issues", issues);
		model.addAttribute("id", userEntity.getId());
		model.addAttribute("usr", userEntity);
		model.addAttribute("userType", userType);
		return "/templates/main_header";
	}

	@RequestMapping(value = { "/footer" }, method = RequestMethod.GET)
	public String footer_GET() {

		return "/templates/footer";
	}

	@RequestMapping(value = "/post-new-idea", method = RequestMethod.GET)
	public String postAnIdea(HttpSession session) {
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_STAFF")) {
				return "redirect:/staff/post-new-idea";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				return "redirect:/student/post-new-idea";
			}
		}
		return "redirect:/dashboard";
	}

	@RequestMapping(value = { "/{userType}/update-profile", "/{userType}/change-password",
			"/{userType}/change-profile-picture" }, method = RequestMethod.GET)
	public String updateProfile_GET(@PathVariable("userType") String userType, Model model, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("userType", userType);
		model.addAttribute("msg", "");
		model.addAttribute("cssClass", "");
		return "/templates/update_profile";
	}

	@RequestMapping(value = "/{userType}/change-password", method = RequestMethod.POST)
	public String changePassword_POST(Model model, @PathVariable("userType") String userType,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("inputNewPassword") String newPassword,
			@RequestParam("reInputNewPassword") String reNewPassword, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		if (encoder.matches(oldPassword, userEntity.getPassword())) {
			if (newPassword.equals(reNewPassword)) {
				userEntity.setPassword(encoder.encode(newPassword));
				userService.updateUser(userEntity);
				model.addAttribute("msg", "Password changed successfully.");
				model.addAttribute("cssClass", "alert alert-success");
				System.out.println("Password changed");
			} else {
				model.addAttribute("msg", "Sorry, the new passphrases did not match.");
				model.addAttribute("cssClass", "alert alert-danger");
				System.out.println("Password not changed");
			}
		} else {
			model.addAttribute("msg", "Sorry, the old password you entered doesn't exist.");
			model.addAttribute("cssClass", "alert alert-danger");
		}
		model.addAttribute("userType", userType);
		return "/templates/update_profile";
	}

	@RequestMapping(value = { "/{userType}/change-profile-picture" }, method = RequestMethod.POST)
	public String updateProfilePhoto_POST(Model model, HttpSession session,
			@RequestParam(name = "image") MultipartFile file) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Attachment attachment = new Attachment();
		attachment.setAttachmentId(userEntity.getUserImage().getAttachmentId());
		attachment.setFileName("" + userEntity.getUserImage().getAttachmentId());
		attachment.setFileTitle(Long.toString(userEntity.getId()));
		attachmentService.save(attachment, file, userEntity.getId());
		userEntity.setUserImage(attachment);
		userService.creteOrUpdate(userEntity);
		return "redirect:/dashboard";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/control-sidebar")
	public String controlSideBar_GET() {
		return "templates/control_sidebar";
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "/ideas", method = RequestMethod.GET)
	public String ideas_GET(HttpSession session, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		int resultPerPage = 5;

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
		
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		
		for (Idea idea : ideas) {

			List<Comment> comments = new ArrayList<>();
			authorities.forEach(authority->{
				if(authority.getAuthority().equals("ROLE_STUDENT")) {
					for (Comment comment : idea.getComments()) {
						for (Authority authority2 : comment.getCommentedUser().getAuthorities()) {
							if(authority2.getAuthority().equals("ROLE_STUDENT")) {
								comments.add(comment);
								break;
							}
						}
					}
					idea.setComments(comments);
				}else {
					idea.setComments(commentService.findAllByIdea(idea));
				}
			});
			
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

				index = 0;
				timeline.setTotalComments(idea.getComments().size());

				dates.get(dateTimeString.split(" ")[0]).add(timeline);

			}
		}
		int totalResults = ideas.size();

		String userType = "";
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				userType = "admin";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				userType = "student";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_MANAGER")) {
				userType = "qa_manager";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_COORDINATOR")) {
				userType = "qa_coordinator";
			}
		}

		int pages = (int) Math.ceil(((double) countIdeas) / resultPerPage);

		model.addAttribute("pages", totalResults == 5 ? pages - 1 : resultPerPage == totalResults ? 0 : pages - 1);

		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);
		model.addAttribute("userType", userType);
		logger.info("templates -> ideas : ");

		return "/templates/ideas";
	}

	@RequestMapping(value = "/ideas/{id}")
	public String readFullPost_GET(HttpSession session, Model model, @PathVariable("id") Long id,
			@RequestParam(name = "notif_id", defaultValue = "") String notificationId) {

		@SuppressWarnings("unchecked")
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		if (!notificationId.isEmpty() && Character.isDigit(notificationId.charAt(0))) {
			Notification notification = notificationService.findById(Long.parseLong(notificationId));
			notification.setSeen("yes");
			notificationService.update(notification);
		}

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Idea idea = ideaService.getIdea(id);
		idea.getComments().clear();
		List<Comment> comments = new ArrayList<>();
		authorities.forEach(authority->{
			if(authority.getAuthority().equals("ROLE_STUDENT")) {
				for (Comment comment : idea.getComments()) {
					for (Authority authority2 : comment.getCommentedUser().getAuthorities()) {
						if(authority.getAuthority().equals("ROLE_STUDENT")) {
							comments.add(comment);
							break;
						}
					}
				}
				idea.setComments(comments);
			}else {
				idea.setComments(commentService.findAllByIdea(idea));
			}
		});
		
		idea.getSeenBy().add(userEntity.getUsername());
		idea.setCountViews(idea.getCountViews() + 1);
		ideaService.update(idea);
		
		Timeline timeline = new Timeline(
				utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa"), idea);
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

		Set<Attachment> attachments = new HashSet<Attachment>();
		for (Attachment attachment : idea.getAttachments()) {
			if (null != attachment.getFileType()) {
				attachments.add(attachment);
			}
		}
		idea.setAttachments(attachments);
		if (idea.getTag().getFinalClosingDate().getTime() < new Date().getTime()) {
			timeline.setTagExpired(true);
		}

		timeline.setTotalComments(idea.getComments().size());

		String userType = "";
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				userType = "admin";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				userType = "student";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_MANAGER")) {
				userType = "qa_manager";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_COORDINATOR")) {
				userType = "qa_coordinator";
			}
		}
		List<Idea> ideasByTag = ideaService.ideasByTag(idea.getTag());
		model.addAttribute("ideaSubmittedBy", userService.getUserByEmail(idea.getAuthorEmail()));
		model.addAttribute("usr", userEntity);
		model.addAttribute("timeline", timeline);
		model.addAttribute("userType", userType);
		model.addAttribute("totalAttachments", idea.getAttachments().size());
		model.addAttribute("ideas", ideasByTag);
		return "/templates/read_full_post";
	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile_GET(HttpSession session, Model model, @RequestParam("id") String username,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = userService.getUserByUsername(username);
		UserEntity userEntity2 = (UserEntity) session.getAttribute("usr");
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
					if (reaction.getReactedUser().getUsername().equals(userEntity2.getUsername())) {
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
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		String userType = "";
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				userType = "admin";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				userType = "student";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_MANAGER")) {
				userType = "qa_manager";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_COORDINATOR")) {
				userType = "qa_coordinator";
			}
		}

		model.addAttribute("totalIdeas", ideas2.size());
		model.addAttribute("totalTags", tags.size());
		model.addAttribute("totalContributions", contributionService.findContributionsByUser(userEntity).size());

		// timeline data

		model.addAttribute("pages", totalResults == 5 ? pages - 1 : resultPerPage == totalResults ? 0 : pages - 1);

		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity2);
		model.addAttribute("requestedUser", userEntity);
		model.addAttribute("userType", userType);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);
		logger.info("Student -> profile : ");

		return "/templates/profile";
	}

	@RequestMapping("/side-nav")
	public String sideNav(Model model, HttpSession session) {
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");

		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				return "/templates/side_nav_admin";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_MANAGER")) {
				return "/templates/side_nav_qa_manager";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_QA_COORDINATOR")) {
				return "/templates/side_nav_qa_coordinator";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				return "/templates/side_nav_student";
			}
		}
		return "/templates/side_nav_student";
	}

	int ideasByDepartment = 0;
	@RequestMapping(value= {"/qa_manager/number-of-ideas-by-department","/qa_coordinator/number-of-ideas-by-department"})
	public @ResponseBody Integer totalNumberOfIdeasByDepartment(HttpSession session,
			@RequestParam(name = "departmentName", defaultValue = "") String departmentName) {
		ideasByDepartment = 0;
		ideaService.listAllIdeas().forEach(idea -> {
			if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment().contains(departmentName)) {
				ideasByDepartment++;
			}
		});
		return ideasByDepartment;
	}
	
	int percentageOfIdeasByDepartment = 0;

	@RequestMapping(value= {"/qa_manager/percentage-of-ideas-by-department","/qa_coordinator/percentage-of-ideas-by-department"})
	public @ResponseBody Integer totalPercentageOfIdeasByDepartment(HttpSession session,
			@RequestParam(name = "departmentName", defaultValue = "") String departmentName) {
		percentageOfIdeasByDepartment = 0;
		ideaService.listAllIdeas().forEach(idea -> {
			if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment().contains(departmentName)) {
				percentageOfIdeasByDepartment++;
			}
		});
		return (ideaService.listAllIdeas().size() > 0 ? ((percentageOfIdeasByDepartment * 100) / ideaService.listAllIdeas().size())
				: ideaService.listAllIdeas().size());
	}

	int numberOfContribution = 0;
	@RequestMapping(value= {"/qa_manager/number-of-contributions-by-department","/qa_coordinator/number-of-contributions-by-department"})
	public @ResponseBody Integer totalContributionsByDepartment(HttpSession session,
			@RequestParam(name = "departmentName", defaultValue = "") String departmentName) {
		numberOfContribution = 0;

		ideaService.listAllIdeas().forEach(idea -> {
			commentService.findAllByIdea(idea).forEach(comment -> {
				if (comment.getCommentedUser().getDepartment().contains(departmentName)) {
					numberOfContribution++;
				}
			});

			if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment().contains(departmentName)) {
				numberOfContribution++;
			}
		});

		return numberOfContribution;
	}

	int ideasWithoutComment = 0;
	@RequestMapping(value= {"/qa_manager/number-of-ideas-without-comment","/qa_coordinator/number-of-ideas-without-comment"})
	public @ResponseBody Integer totalNumberOfIdeasWithoutComment(HttpSession session,
			@RequestParam(name = "departmentName", defaultValue = "") String departmentName) {
		ideasWithoutComment = 0;

		if (!departmentName.isEmpty()) {
			ideaService.listAllIdeas().forEach(idea -> {
				if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment().contains(departmentName)) {
					if (idea.getComments().size() < 1) {
						ideasWithoutComment++;
					}
				}
			});
		} else {
			ideaService.listAllIdeas().forEach(idea -> {
				if (idea.getComments().size() < 1) {
					ideasWithoutComment++;
				}
			});
		}

		return ideasWithoutComment;
	}

	int numberOfAnonymousComments = 0;
	@RequestMapping(value= {"/qa_manager/number-of-anonymous-comments", "/qa_coordinator/number-of-anonymous-comments"})
	public @ResponseBody Integer totalAnonymousComments(HttpSession session,
			@RequestParam(name = "departmentName", defaultValue = "") String departmentName) {
		numberOfAnonymousComments = 0;

		if (!departmentName.isEmpty()) {
			ideaService.listAllIdeas().forEach(idea -> {
				if (userService.getUserByEmail(idea.getAuthorEmail()).getDepartment().contains(departmentName)) {
					idea.getComments().forEach(comment -> {
						if (comment.isAnonymous() == true) {
							numberOfAnonymousComments++;
						}
					});
				}
			});
		} else {
			ideaService.listAllIdeas().forEach(idea -> {
				idea.getComments().forEach(comment -> {
					if (comment.isAnonymous() == true) {
						numberOfAnonymousComments++;
					}
				});
			});
		}

		return numberOfAnonymousComments;
	}	

}
