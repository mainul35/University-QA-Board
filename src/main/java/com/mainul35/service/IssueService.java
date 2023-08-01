package com.mainul35.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainul35.entity.Issue;
import com.mainul35.entity.UserEntity;
import com.mainul35.repository.IssueRepository;

@Service
public class IssueService {

	@Autowired
	private IssueRepository issueRepository;
	
	public void save(Issue issue) {
		issueRepository.save(issue);
	}
	
	public List<Issue> findAllIssues(){
		return issueRepository.findAll();
	}
	
	public Issue findById(Long issueId) {
		return issueRepository.findById(issueId).orElse(null);
	}
	
	public List<Issue> findByIssueSubmittedBy(UserEntity userEntity){
		return issueRepository.findByIssueSubmittedBy(userEntity);
	}

}
