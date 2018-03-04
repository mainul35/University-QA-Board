package com.springprojects.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	
	public void update(Idea idea) {
		ideaRepository.save(idea);
	}

	public List<Idea> listAllIdeas(){
		return (List<Idea>) ideaRepository.findAll();
	}
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail, int pageNumber, int resultPerPage){
		return (List<Idea>) ideaRepository.findAllByAuthorEmailOrderByPublishingDateDesc(authorEmail, getPage(pageNumber, resultPerPage)).getContent();
	}
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail){
		return (List<Idea>) ideaRepository.findAllByAuthorEmail(authorEmail);
	}
	
	public PageRequest getPage(int pageNumber, int resultPerPage) {
        PageRequest request = new PageRequest(pageNumber - 1,  resultPerPage, Sort.Direction.ASC, "publishingDate");
        return request;
    }
	
	public int count(){
        return listAllIdeas().size();
    }
	
	public int count(String authorEmail, int pageNumber, int resultPerPage) {
		return listAllIdeasByAuthorEmail(authorEmail, pageNumber, resultPerPage).size();
	}
	
	public Idea getIdea(Long ideaId) {
		return ideaRepository.findOne(ideaId);
	}
}
