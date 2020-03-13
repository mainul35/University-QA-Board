package com.springprojects.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Issue;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.IssueRepository;

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
		return issueRepository.findById(issueId).get();
	}
	
	public List<Issue> findByIssueSubmittedBy(UserEntity userEntity){
		return issueRepository.findByIssueSubmittedBy(userEntity);
	}

}
