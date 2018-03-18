package com.springprojects.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Idea;
import com.springprojects.entity.Tag;
@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long>{

//	@Query("select i from Idea i where i.authorEmail = ?1 Order By i.ideaId DESC")
	Page<Idea> findAllByAuthorEmailOrderByIdeaIdDesc(String email, Pageable pageable);

	List<Idea> findAllByAuthorEmail(String email);
	
	@Query("select i from Idea i")
	Page<Idea> findAll(Pageable page);

	List<Idea> findAllByTag(Tag tag);
}
