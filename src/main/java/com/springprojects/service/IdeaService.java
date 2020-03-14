package com.springprojects.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Idea;
import com.springprojects.entity.Tag;
import com.springprojects.repository.IdeaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

@Service
public class IdeaService {
	@Autowired
	IdeaRepository ideaRepository;
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	
	public void save(Idea idea) {
		if(!ideaRepository.findById(idea.getIdeaId()).isPresent()) {
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
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail, int pageNumber, int resultPerPage){
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Idea> cq = cb.createQuery(Idea.class);
		Root<Idea> root = cq.from(Idea.class);

		cq = cq.select(root)
				.where(cb.equal(root.get("authorEmail"), authorEmail));
		List<Order> orderList = new ArrayList<>();
		orderList.add(cb.desc(root.get("ideaId")));
//		orderList.add(cb.desc(root.get("updatedAt")));
		cq.orderBy(orderList);
		TypedQuery<Idea> ideaTypedQuery = entityManager.createQuery(cq);
		ideaTypedQuery.setFirstResult((pageNumber - 1) * 5);
		ideaTypedQuery.setMaxResults(resultPerPage);
		return ideaTypedQuery.getResultList();
//		return ideaRepository.findAllByAuthorEmailOrderByIdeaIdDesc(authorEmail, getPage(pageNumber, resultPerPage));
	}
	
	public List<Idea> listAllIdeasByAuthorEmail(String authorEmail){
		return ideaRepository.findAllByAuthorEmail(authorEmail);
	}
	
	public PageRequest getPage(int pageNumber, int resultPerPage) {
		return PageRequest.of(pageNumber - 1,  resultPerPage, Sort.Direction.DESC, "ideaId");
    }
	
	public int count(){
        return listAllIdeas().size();
    }
	
	public int count(String authorEmail, int pageNumber, int resultPerPage) {
		return listAllIdeasByAuthorEmail(authorEmail, pageNumber, resultPerPage).size();
	}

	public int count(int pageNumber, int resultPerPage) {
		return ideaRepository.findAll(getPage(pageNumber, resultPerPage)).getContent().size();
	}
	
	public Idea getIdea(Long ideaId) {
		return ideaRepository.findById(ideaId).orElse(null);
	}

	public Page<Idea> getPageOfIdeas(int pageNumber, int resultPerPage) {
		// TODO Auto-generated method stub
		return ideaRepository.findAll(getPage(pageNumber, resultPerPage));
	}
	
	public List<Idea> ideasByTag(Tag tag){
		return ideaRepository.findAllByTag(tag);
	}
}
