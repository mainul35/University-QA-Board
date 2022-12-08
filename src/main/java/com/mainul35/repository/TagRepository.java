package com.mainul35.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{

	Tag findByTagName(String tagName);
	
}
