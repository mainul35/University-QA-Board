package com.springprojects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Idea;
@Repository
public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long>{

}
