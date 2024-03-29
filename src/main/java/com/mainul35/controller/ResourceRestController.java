package com.mainul35.controller;

import com.mainul35.config.Utils;
import com.mainul35.entity.Attachment;
import com.mainul35.entity.UserEntity;
import com.mainul35.service.ActivityService;
import com.mainul35.service.AttachmentService;
import com.mainul35.service.IdeaService;
import com.mainul35.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
//@RequestMapping("/users")
public class ResourceRestController {

    @Autowired
	private Utils utils;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private IdeaService ideaService;
    @Autowired
    private ActivityService activityService;

	@RequestMapping(value="/terms-and-conditions", method=RequestMethod.GET)
	@ResponseBody
	public String termsAndConditions_GET() {
		System.out.println("---------------------------------------------");
		String tndc = utils.readFile("/TermsAndConditions.txt");
		System.out.println(tndc);
		return tndc;
	}
	
	@RequestMapping(value="/file", method= {RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public byte[] resourceInDrive_GET(@RequestParam(name="fileId") Long fileId) {
		System.out.println("Reading resource from drive...");
		byte[] bs = null;
		try {
			bs = utils.readFile(fileId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bs;
	}
	
	@RequestMapping(value="/videos", method= {RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public List<String> videosOfAnIdea(@RequestParam(name="ideaId") Long ideaId) {
		List<String> videoURLs = new ArrayList<>();
		System.out.println("Sending vieo URLs...");
		Set<Attachment> attachments = ideaService.getIdea(ideaId).getAttachments();
		for(Attachment attachment : attachments) {
			if(attachment.getFileType()!=null && attachment.getFileType().contains("video")) {
				videoURLs.add(attachment.getFileURL());
			}
		}
		return videoURLs;
	}
	
	@RequestMapping(value="/other-attachments", method= {RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public List<String> otherAtachmentsOfAnIdea(HttpServletResponse response, @RequestParam(name="ideaId") Long ideaId) {
		List<String> otherAtachmentURLs = new ArrayList<>();
		System.out.println("Sending attachment URLs...");
		Set<Attachment> attachments = ideaService.getIdea(ideaId).getAttachments();
		for(Attachment attachment : attachments) {
			if(attachment.getFileType()==null) {
				otherAtachmentURLs.add(attachment.getFileURL());
			}else if(!attachment.getFileType().contains("image") && !attachment.getFileType().contains("video")) {
				otherAtachmentURLs.add(attachment.getFileURL());
			}
		}
		return otherAtachmentURLs;
	}
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/activity-interval")
	@ResponseBody
	public String activityInterval(HttpSession session, @RequestParam(name="id", defaultValue="") String username) {
		UserEntity userEntity = null;
		if(username.equals("")) {
			userEntity = (UserEntity) session.getAttribute("usr");			
		}else {
			userEntity = userService.getUserByUsername(username);
		}
		return activityService.getLastActivityDifferenceOfUser(userEntity);
	}
}
