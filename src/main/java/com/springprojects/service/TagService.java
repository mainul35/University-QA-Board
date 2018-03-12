package com.springprojects.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Tag;
import com.springprojects.repository.TagRepository;

@Service
public class TagService {

	@Autowired
	private TagRepository tagRepository;
	
	public boolean save(Tag tag) {
		if(!exists(tag.getTagName())) {
			tagRepository.save(tag);
			return true;
		}else {
			return false;
		}
	}
	
	public Tag findByTagName(String tagName) {
		return tagRepository.findByTagName(tagName);
	}
	
	public boolean exists(String tagName) {
		if(findByTagName(tagName)!=null) {
			return true;
		}else {
			return false;
		}
	}

	public List<Tag> listAllTags() {
		// TODO Auto-generated method stub
		return tagRepository.findAll();
	}
}
