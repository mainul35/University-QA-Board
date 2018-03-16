package com.springprojects.controller;

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

import com.springprojects.entity.Activity;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Contribution;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.CommentService;
import com.springprojects.service.ContributionService;
import com.springprojects.service.IdeaService;
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
	public List<Comment> comments_GET(@PathVariable("ideaId") Long ideaId, @RequestParam("uid") String username) {
		Idea idea = ideaService.getIdea(ideaId);
		List<Comment> comments2 = new ArrayList<>();
		for (Comment comment2 : idea.getComments()) {
			comments2.add(comment2);
		}
		logger.info("Fetching Comments...");
		return comments2;
	}

}
