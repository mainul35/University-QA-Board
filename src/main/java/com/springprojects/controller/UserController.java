package com.springprojects.controller;

import com.springprojects.config.Initializer;
import com.springprojects.config.Utils;
import com.springprojects.customModel.Timeline;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.Tag;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.CommentService;
import com.springprojects.service.IdeaService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	private int index = 0;

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
			Initializer initializer = new Initializer(authorityService, userService, attachmentService, encoder);
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
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		model.addAttribute("username", session.getAttribute("username").toString());
		for (Authority authority : authorities) {
			if (authority.getAuthority().toUpperCase().equals("ROLE_ADMIN")) {
				logger.info("Redirecting to Admin Dashboard ");
				return "redirect:/admin/dashboard";
			} else if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
				logger.info("Redirecting to Student Dashboard ");
				return "redirect:/student/dashboard";
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

	@RequestMapping(value = "/ideas", method = RequestMethod.GET)
	public String ideas_GET(HttpSession session, Model model,
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");

		int resultPerPage = 5;

		List<Idea> ideas = new ArrayList<>();
		ideaService.getPageOfIdeas(pageNumber, resultPerPage).iterator().forEachRemaining(idea -> {
			ideas.add(idea);
		});

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
		int pages = (int) Math.ceil(((double) totalResults) / resultPerPage);
		model.addAttribute("pages",
				resultPerPage == 5 ? ideaService.count(userEntity.getEmail(), pageNumber, resultPerPage) - 1
						: resultPerPage == totalResults ? 0 : pages - 1);
		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
		model.addAttribute("utils", utils);
		logger.info("templates -> ideas : ");

		return "/templates/ideas";
	}

	@RequestMapping(value = "/ideas/{id}")
	public String readFullPost_GET(HttpSession session, Model model, @PathVariable("id") Long id) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Idea idea = ideaService.getIdea(id);
		idea.getComments().clear();
		idea.setComments(commentService.findAllByIdea(idea));
		
		Timeline timeline = new Timeline(utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa"), idea);
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
		
		List<Attachment> otherAttachments = new ArrayList<>();
		for(Attachment attachment : idea.getAttachments()) {
			if(!attachment.getFileType().contains("image") && !attachment.getFileType().contains("video")) {
				otherAttachments.add(attachment);
			}
		}
		
		if (idea.getTag().getFinalClosingDate().getTime() < new Date().getTime()) {
			timeline.setTagExpired(true);
		}
	
		timeline.setTotalComments(idea.getComments().size());		
		
		List<Idea> ideasByTag = ideaService.ideasByTag(idea.getTag());
		model.addAttribute("ideaSubmittedBy", userService.getUserByEmail(idea.getAuthorEmail()));
		model.addAttribute("usr", userEntity);
		model.addAttribute("timeline", timeline);
		model.addAttribute("totalAttachments", idea.getAttachments().size());
		model.addAttribute("otherAttachments", otherAttachments);
		model.addAttribute("ideas", ideasByTag);

		return "/templates/read_full_post";
	}
}
