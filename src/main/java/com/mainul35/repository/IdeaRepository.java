package com.mainul35.repository;

import java.util.List;
import java.util.Optional;

import com.mainul35.repository.util.HibernateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Idea;
import com.mainul35.entity.Tag;
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long>, HibernateRepository<Idea> {

	Page<Idea> findAllByAuthorEmailOrderByIdeaIdDesc(String email, Pageable pageable);

	List<Idea> findAllByAuthorEmail(String email);
	
	@Query("select i from Idea i")
	Page<Idea> findAll(Pageable page);

	List<Idea> findAllByTag(Tag tag);

	List<Idea> findByTag(Tag tag);

	Optional<Idea> findByIdeaId(Long id);
}
