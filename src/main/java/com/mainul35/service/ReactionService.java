package com.mainul35.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainul35.entity.Reaction;
import com.mainul35.repository.ReactionRepository;

@Service
public class ReactionService {

	@Autowired
	private ReactionRepository reactionRepository;
	
	@Autowired
	private IdeaService ideaService;
	
	@Autowired
	private UserService userService;
	
	public Reaction findOne(Long id) {
		return reactionRepository.findById(id).orElse(null);
	}
	
	public void save(Reaction reaction) {
		reactionRepository.save(reaction);
	}
	
	public boolean exists(Long userId, Long ideaId) {
		for(Reaction reaction : ideaService.getIdea(ideaId).getReactions()) {
			if(reaction.getReactedUser().getId().equals(userId)) {
				return true;
			}
		}
		return false;
	}

	public Reaction fetch(Long ideaId, String username) {
		// TODO Auto-generated method stub
		return reactionRepository.findByIdeaAndReactedUser(ideaService.getIdea(ideaId), userService.getUserByUsername(username));
	}
}
