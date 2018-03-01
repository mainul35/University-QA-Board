package com.springprojects.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.set.MapBackedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springprojects.config.Utils;
import com.springprojects.customModel.Timeline;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Comment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.Tag;
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
	public String studentDashboard_GET() {
		return "redirect:/student/activity";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/activity")
	public String studentActivity_GET(Model model, HttpSession session) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("usr", userEntity);
		logger.info("Student -> Activity : ");
		return "/student_template/activity";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/post-new-idea")
	public String postNewIdea_GET(Model model, HttpSession session) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		model.addAttribute("idea", new Idea());
		model.addAttribute("usr", userEntity);
		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(
				tag -> tag.getClosingDate() == null || tag.getClosingDate().getTime() < System.currentTimeMillis());
		
		model.addAttribute("categories", tags);
		logger.info("Student -> Post new idea : ");
		return "/student_template/post_an_idea";
	}
	
	@RequestMapping(value = "/post-new-idea", method = RequestMethod.POST)
	public String postNewIdea_POST(Model model, HttpSession session, @ModelAttribute("idea") Idea idea,
			@RequestParam(name = "tagName", defaultValue = "") String tagName,
			@RequestParam(name = "publishingDateTime") String publishingDateTime,
			@RequestParam(name = "images[]") MultipartFile[] files) {

		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		Tag tag = tagService.findByTagName(tagName);
		if (tag.getClosingDate().getTime() < System.currentTimeMillis()) {
			model.addAttribute("ok", "false");
			model.addAttribute("msg", "Sorry, The category expired.");
			return "";
		}
		idea.setTag(tagService.findByTagName(tagName));
		idea.setAuthorEmail(userEntity.getEmail());
		idea.setCountViews(0);
		idea.setIdeaId(System.currentTimeMillis());
		idea.setPublishingDate(utils.convertStringToTimestamp(publishingDateTime, "dd-MM-yyyy HH:mm:ss"));

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

		List<Tag> tags = tagService.listAllTags();
		tags.removeIf(t -> t.getClosingDate() == null || t.getClosingDate().getTime() < System.currentTimeMillis());

		idea.setAttachments(attachments);
		ideaService.save(idea);
		logger.info("Student -> Post new idea : ");
		return "redirect:/student/timeline";
	}

	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public String timeline_GET(
			HttpSession session, 
			Model model, 
			@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
		UserEntity userEntity = (UserEntity) session.getAttribute("usr");
		
		int resultPerPage = 5;
		
		List<Idea> ideas = (List<Idea>) ideaService.listAllIdeasByAuthorEmail(userEntity.getEmail(), pageNumber, resultPerPage);

		Map<String, List<Timeline>> dates = new TreeMap<>(Comparator.reverseOrder());

		String dateTimeString = "";

		for (Idea idea : ideas) {

			dateTimeString = utils.convertTimestampToString(idea.getPublishingDate(), "d/MM/YYYY hh:mm:ss aaa");
			String time = dateTimeString.split(" ")[1] + " " + dateTimeString.split(" ")[2];

			if (!dates.containsKey(dateTimeString.split(" ")[0])) {
				dates.put(dateTimeString.split(" ")[0], new ArrayList());
			}
			if (dates.containsKey(dateTimeString.split(" ")[0])) {
				Timeline timeline = new Timeline(time, idea);
				
				for(Reaction reaction :idea.getReactions()) {
					if(reaction.getReactionType()==1) {
						timeline.setTotalThumbUp(timeline.getTotalThumbUp()+1);
					}else if(reaction.getReactionType()==2){
						timeline.setTotalThumbDown(timeline.getTotalThumbDown()+1);
					}
					if(reaction.getReactedUser().getUsername().equals(userEntity.getUsername())) {
						timeline.setReactionOfCurrentUser(reaction.getReactionType());
					}
				}
				
				timeline.setTotalComments(idea.getComments().size());
				
				dates.get(dateTimeString.split(" ")[0]).add(timeline);
			}
		}
		int totalResults = ideas.size();
        int pages =(int)Math.ceil(((double)totalResults) / resultPerPage);
        model.addAttribute("pages",
                resultPerPage == 5
                ?ideaService.count(userEntity.getEmail(), pageNumber, resultPerPage)-1:resultPerPage==totalResults
                ?0:pages-1);
        model.addAttribute("currentPage", pageNumber);
        
		model.addAttribute("usr", userEntity);
		model.addAttribute("dates", dates);
//		model.addAttribute("reactions", attributeValue);
		logger.info("Student -> timeline : ");

		return "/student_template/timeline";
	}
}
