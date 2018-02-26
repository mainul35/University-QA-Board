package com.springprojects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Idea;
@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long>{

//	@Query("select i from Idea i where i.authorEmail = ?1 Order By ASC")
	List<Idea> findAllByAuthorEmailOrderByPublishingDateDesc(String email);
}
