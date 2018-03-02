package com.springprojects.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Comment;
import com.springprojects.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	IdeaService ideaService;
	
	@Autowired
	UserService userService;
	
	public Comment findOne(Long id) {
		return commentRepository.findOne(id);
	}
	
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public boolean exists(Long userId, Long ideaId) {
		for(Comment comment : ideaService.getIdea(ideaId).getComments()) {
			if(comment.getCommentedUser().getId().equals(userId)) {
				return true;
			}
		}
		return false;
	}

	public Comment fetch(Long ideaId, String username) {
		// TODO Auto-generated method stub
		return commentRepository.findByIdeaAndCommentedUser(ideaService.getIdea(ideaId), userService.getUserByUsername(username));
	}

}
