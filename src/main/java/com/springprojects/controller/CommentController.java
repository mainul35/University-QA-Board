package com.springprojects.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springprojects.config.Mailer;
import com.springprojects.entity.Activity;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Contribution;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Notification;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.CommentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.NotificationService;
import com.springprojects.service.UserService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private IdeaService ideaService;
	@Autowired
	private ContributionService contributionService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private NotificationService notificationService;
	
	private final Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(value = "/{usertype}/ideas/{ideaId}/comments", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<List<Comment>> comments_POST(
			HttpSession session,
			@PathVariable("ideaId") Long ideaId, 
			@RequestParam("uid") String username,
			@RequestParam(name = "anonymous", 
			defaultValue = "false") Boolean isAnonymous,
			@RequestBody Comment comment
			) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Set<Comment> comments = new HashSet<>();
		System.out.println(comment);
		Idea idea = ideaService.getIdea(ideaId);
		if (comment.getCommentBody().isEmpty() == false
				&& comment.getCommentDateTime().getTime() > idea.getTag().getFinalClosingDate().getTime() == false) {
			comment.setIdea(ideaService.getIdea(ideaId));
			comment.setAnonymous(isAnonymous);
			comment = commentService.save(comment);
			Contribution contribution = new Contribution(comment.getCommentId(), userEntity);
			contributionService.save(contribution );
			Activity activity = new Activity();
			activity.setId(userEntity.getId());
			activity.setLastActivityDateTime(new Timestamp(System.currentTimeMillis()));
			activityService.saveOrUpdate(activity);
			comments.add(comment);
			idea.getComments().add(comment);
			ideaService.update(idea);
			
			InetAddress IP = null;
			try {
				IP = Inet4Address.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Mailer.sendMail(
					idea.getAuthorEmail(), 
					"EWSD - "+comment.getCommentedUser().getName()+" Commented on your idea -\""+idea.getIdeaTitle()+"\"",
					"To read the comment, please click on the link below. \n http://ec2-18-220-231-146.us-east-2.compute.amazonaws.com:8080/ewsd/ideas/"+idea.getIdeaId());
			
			Notification notification = new Notification();
			notification.setNotificationId(comment.getCommentId());
			notification.setNotificationMsg("Commented on your idea - "+idea.getIdeaTitle());
			notification.setNotificationType("notification");
			notification.setNotificationUrl("http://ec2-18-220-231-146.us-east-2.compute.amazonaws.com:8080/ewsd/ideas/"+idea.getIdeaId());
			notification.setNotifyTo(userService.getUserByEmail(idea.getAuthorEmail()));
			notification.setNotificationFrom(comment.getCommentedUser());
			notification.setSeen("no");
			
			notificationService.save(notification);
			List<Comment> comments2 = new ArrayList<>();
			for (Comment comment2 : idea.getComments()) {
				comments2.add(comment2);
			}
			logger.info("Comment posted");
			return new ResponseEntity<List<Comment>>(comments2, HttpStatus.OK);
		} else if (comment.getCommentBody().isEmpty()) {
			return new ResponseEntity<List<Comment>>(HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Comment>>(HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@RequestMapping(value = "/{usertype}/ideas/{ideaId}/comments", method = RequestMethod.GET, produces = "application/json")
	public List<Comment> comments_GET(@PathVariable("ideaId") Long ideaId, @RequestParam("uid") String username, HttpSession session) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Idea idea = ideaService.getIdea(ideaId);
		List<Comment> comments2 = new ArrayList<>();
		List<Authority> authorities = (List<Authority>) session.getAttribute("authorities");
		for (Comment comment2 : idea.getComments()) {
			for (Authority authority : authorities) {
				if (authority.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
					List<Authority> authorities2 = (List<Authority>)comment2.getCommentedUser().getAuthorities();
					for (Authority authority2 : authorities2) {
						if (authority2.getAuthority().toUpperCase().equals("ROLE_STUDENT")) {
							comments2.add(comment2);							
						}
					}
				}else {
					comments2.add(comment2);
				}
			}
			
		}
		logger.info("Fetching Comments...");
		return comments2;
	}

}
