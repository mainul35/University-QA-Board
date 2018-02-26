package com.springprojects.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Idea;
import com.springprojects.repository.IdeaRepository;

@Service
public class IdeaService {
	@Autowired
	IdeaRepository ideaRepository;
	
	public void save(Idea idea) {
		if(!ideaRepository.exists(idea.getIdeaId())) {
			ideaRepository.save(idea);
		}
	}
	
	public List<Idea> listAllIdeas(){
		return (List<Idea>) ideaRepository.findAll();
	}
	
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail){
		return (List<Idea>) ideaRepository.findAllByAuthorEmailOrderByPublishingDateDesc(authorEmail);
	}
}
