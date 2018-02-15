package com.springprojects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

	Tag findByTagName(String tagName);

}
