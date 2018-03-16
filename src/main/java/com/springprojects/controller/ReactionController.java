package com.springprojects.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springprojects.config.Utils;
import com.springprojects.entity.Activity;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.ActivityService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.ReactionService;
import com.springprojects.service.UserService;

@RestController
public class ReactionController {

	@Autowired
	private ReactionService reactionService;
	@Autowired
	private UserService userService;
	@Autowired
	private IdeaService ideaService;
	@Autowired
	Utils utils;
	@Autowired
	private ActivityService activityService;
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	@RequestMapping(value="/{usertype}/ideas/{ideaId}/reactions", method=RequestMethod.GET)
	public List<Reaction> reactions(
			HttpSession session,
			@PathVariable("ideaId") Long ideaId, 
			@RequestParam("uid") String username, 
			@RequestParam(name="reactionType", defaultValue="0") Integer reactionType
			){
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Set<Reaction> reactionsSet = new HashSet<>();
		
		Idea idea = ideaService.getIdea(ideaId);
		if(!reactionService.exists(userService.getUserByUsername(username).getId(), ideaId)) {
			Reaction reaction = new Reaction();
			reaction.setReactionType(reactionType);
			reaction.setReactedUser(userService.getUserByUsername(username));
			reaction.setIdea(ideaService.getIdea(ideaId));
			reaction.setReactionId(System.currentTimeMillis());
			reactionService.save(reaction);
			Activity activity = new Activity();
			activity.setId(userEntity.getId());
			activity.setLastActivityDateTime(new Timestamp(System.currentTimeMillis()));
			activityService.saveOrUpdate(activity);
			reactionsSet.add(reaction);
			idea.setReactions(reactionsSet);
			ideaService.update(idea);
		}else{
			Reaction reaction = reactionService.fetch(ideaId, username);
			reaction.setReactionType(reactionType);
			reactionService.save(reaction);
		}
		
		List<Reaction> reactions = new ArrayList<>();
		for(Reaction reaction2 : idea.getReactions()) {
			reactions.add(reaction2);
		}
		logger.info("reactions posted");
		return reactions;	
	}
}
