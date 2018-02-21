package com.springprojects.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springprojects.config.Utils;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.IdeaService;
import com.springprojects.service.TagService;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	TagService tagService;
	@Autowired
	Utils utils;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	IdeaService ideaService;
	
	Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(method = RequestMethod.GET, value = "/dashboard")
	public String studentDashboard_GET(Model model, HttpSession session) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("idea", new Idea());
		model.addAttribute("usr", userEntity);
		model.addAttribute("categories", tagService.listAllTags());
		logger.info("Student Dashboard : ");
		return "/student_template/index";
	}

	@RequestMapping(value = "/post-new-idea", method = RequestMethod.POST)
	public String postNewIdea_POST(Model model, HttpSession session, @ModelAttribute("idea") Idea idea,
			@RequestParam(name = "tagName", defaultValue = "") String tagName,
			@RequestParam(name = "publishingDateTime") String publishingDateTime,
			@RequestParam(name = "images[]") MultipartFile[] files) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		idea.setTag(tagService.findByTagName(tagName));
		idea.setAuthorEmail(userEntity.getEmail());
		idea.setCountViews(0);
		idea.setIdeaId(System.currentTimeMillis());
		idea.setPublishingDate(utils.convertDateTimeToTimestamp(publishingDateTime));

		Set<Attachment> attachments = new HashSet<>();
		if (files[0].getOriginalFilename().contains(".")) {
			for (MultipartFile file : files) {
				Attachment attachment = new Attachment();
				Long attachmentId = System.currentTimeMillis();
				attachment.setAttachmentId(attachmentId);
				attachment.setFileName("" + attachmentId);
				attachment.setFileTitle(idea.getIdeaTitle());
				attachment = attachmentService.save(attachment, file, userEntity.getId());
				attachments.add(attachment);
			}
			
		}
		
		idea.setAttachments(attachments);
		ideaService.save(idea);
		model.addAttribute("idea", idea);
		
		System.out.println(idea.toString());
		model.addAttribute("usr", userEntity);
		model.addAttribute("categories", tagService.listAllTags());
		logger.info("Student Dashboard : ");
		return "/student_template/index";
	}
}
