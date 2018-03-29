package com.springprojects.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Issue;
import com.springprojects.entity.UserEntity;

@Transactional
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
	
	@Query("SELECT i FROM Issue i WHERE NOT i.issueStatus = ?1 ")
	List<Issue> findByIssueStatus(String issueStatus);

	List<Issue> findByIssueSubmittedBy(UserEntity userEntity);
}
