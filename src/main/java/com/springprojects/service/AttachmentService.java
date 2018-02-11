package com.springprojects.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Attachment;
import com.springprojects.repository.AttachmentRepository;

@Service
public class AttachmentService {

	@Autowired
	AttachmentRepository attachmentRepository;
	
	public void save(Attachment attachment) {
		attachmentRepository.save(attachment);
	}
	
	
}
