package com.mainul35.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Issue;
import com.mainul35.entity.UserEntity;

@Transactional
public interface IssueRepository extends JpaRepository<Issue, Long> {
	
	@Query("SELECT i FROM Issue i WHERE NOT i.issueStatus = ?1 ")
	List<Issue> findByIssueStatus(String issueStatus);

	List<Issue> findByIssueSubmittedBy(UserEntity userEntity);
}
