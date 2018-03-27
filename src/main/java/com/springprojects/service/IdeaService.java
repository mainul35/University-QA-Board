package com.springprojects.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Idea;
import com.springprojects.entity.Tag;
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
	
	public List<Idea> listIdeasByTag(Tag tag){
		return (List<Idea>) ideaRepository.findByTag(tag);
	}
	
	public Page<Idea> listAllIdeasByAuthorEmail(String authorEmail, int pageNumber, int resultPerPage){
		return ideaRepository.findAllByAuthorEmailOrderByIdeaIdDesc(authorEmail, getPage(pageNumber, resultPerPage));
	}
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail){
		return ideaRepository.findAllByAuthorEmail(authorEmail);
	}
	
	public PageRequest getPage(int pageNumber, int resultPerPage) {
        PageRequest request = new PageRequest(pageNumber - 1,  resultPerPage, Sort.Direction.DESC, "ideaId");
        return request;
    }
	
	public int count(){
        return listAllIdeas().size();
    }
	
	public int count(String authorEmail, int pageNumber, int resultPerPage) {
		return listAllIdeasByAuthorEmail(authorEmail, pageNumber, resultPerPage).getContent().size();
	}

	public int count(int pageNumber, int resultPerPage) {
		return ideaRepository.findAll(getPage(pageNumber, resultPerPage)).getContent().size();
	}
	
	public Idea getIdea(Long ideaId) {
		return ideaRepository.findOne(ideaId);
	}

	public Page<Idea> getPageOfIdeas(int pageNumber, int resultPerPage) {
		// TODO Auto-generated method stub
		return ideaRepository.findAll(getPage(pageNumber, resultPerPage));
	}
	
	public List<Idea> ideasByTag(Tag tag){
		return ideaRepository.findAllByTag(tag);
	}
}
