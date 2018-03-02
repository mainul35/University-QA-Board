package com.springprojects.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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

import com.springprojects.entity.Comment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.service.CommentService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.UserService;

@RestController
public class CommentController {

	@Autowired
	CommentService commentService;
	@Autowired
	UserService userService;
	@Autowired
	IdeaService ideaService;

	private final Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(value = "/{usertype}/ideas/{ideaId}/comments", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody public ResponseEntity comments_POST(@PathVariable("ideaId") Long ideaId, @RequestParam("uid") String username,
			@RequestParam(name = "anonymous", defaultValue = "false") Boolean isAnonymous,
			@RequestBody Comment comment) {
		Set<Comment> comments = new HashSet<>();
		System.out.println(comment);
		Idea idea = ideaService.getIdea(ideaId);
		if(comment.getCommentDateTime().getTime()>idea.getTag().getFinalClosingDate().getTime()==false) {
			comment.setIdea(ideaService.getIdea(ideaId));
			 comment.setAnonymous(isAnonymous);
			 comment = commentService.save(comment);
			 comments.add(comment);
			 idea.getComments().add(comment);
			 ideaService.update(idea);
			List<Comment> comments2 = new ArrayList<>();
			for (Comment comment2 : idea.getComments()) {
				comments2.add(comment2);
			}
			logger.info("Comment posted");
			return new ResponseEntity(comments2, HttpStatus.OK);
		}else {
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
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
