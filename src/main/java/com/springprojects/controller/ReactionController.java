package com.springprojects.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.service.IdeaService;
import com.springprojects.service.ReactionService;
import com.springprojects.service.UserService;

@RestController
public class ReactionController {

	@Autowired
	ReactionService reactionService;
	@Autowired 
	UserService userService;
	@Autowired
	IdeaService ideaService;
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	@RequestMapping(value="/{usertype}/ideas/{ideaId}/reactions", method=RequestMethod.GET)
	public List<Reaction> reactions(
			@PathVariable("ideaId") Long ideaId, 
			@RequestParam("uid") String username, 
			@RequestParam(name="reactionType", defaultValue="0") Integer reactionType
			){

		Set<Reaction> reactionsSet = new HashSet<>();
		
		Idea idea = ideaService.getIdea(ideaId);
		if(!reactionService.exists(userService.getUserByUsername(username).getId(), ideaId)) {
			Reaction reaction = new Reaction();
			reaction.setReactionType(reactionType);
			reaction.setReactedUser(userService.getUserByUsername(username));
			reaction.setIdea(ideaService.getIdea(ideaId));
			reaction.setReactionId(System.currentTimeMillis());
			reactionService.save(reaction);
			
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
