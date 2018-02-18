package com.springprojects.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.UserService;

public class Initializer {

	public Initializer(AuthorityService authorityService, UserService userService, AttachmentService attachmentService, PasswordEncoder encoder) {
		// TODO Auto-generated constructor stub
		
		authorityService.create(new Authority(System.nanoTime(), "ROLE_ADMIN"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_QA_MANAGER"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_QA_COORDINATOR"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_STUDENT"));
		UserEntity userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Admin");
		userEntity.setEmail("teamg5.bit@gmail.com");
		userEntity.setDepartment("admin");
		Set<Authority> authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_ADMIN"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("admin");

		Attachment userImage = new Attachment();
		Long attachmentId = System.currentTimeMillis();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Admin image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		attachmentService.save(userImage);
		userEntity.setUserImage(userImage);
		userService.createUser(userEntity);

		
		//############################	 Insert Student manager data
		
		//Student 1
		
		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Mainul Hasan");
		userEntity.setEmail("mainuls18@gmail.com");
		userEntity.setDepartment("BIT");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_STUDENT"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("mainul35");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Mainul Image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		attachmentService.save(userImage);
		userEntity.setUserImage(userImage);
		userService.createUser(userEntity);
		
		
		//Student 2
		
		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Sazzad Hossain");
		userEntity.setEmail("sazzadhossainsakib@gmail.com");
		userEntity.setDepartment("BIT");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_STUDENT"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("sazzad");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Sazzad image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		attachmentService.save(userImage);
		userEntity.setUserImage(userImage);
		userService.createUser(userEntity);
		
		//User 3
		
		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Tanveer Rahman");
		userEntity.setEmail("tanveershuvos@gmail.com");
		userEntity.setDepartment("BIT");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_STUDENT"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("tanveer");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Sazzad image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		attachmentService.save(userImage);
		userEntity.setUserImage(userImage);
		userService.createUser(userEntity);
		
		//QA Manager
		
		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("QA Manager");
		userEntity.setEmail("qa.manager.bit37@gmail.com");
		userEntity.setDepartment("QA");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_QA_MANAGER"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("qa_manager");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("qa manager image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		attachmentService.save(userImage);
		userEntity.setUserImage(userImage);
		userService.createUser(userEntity);
	}
}
