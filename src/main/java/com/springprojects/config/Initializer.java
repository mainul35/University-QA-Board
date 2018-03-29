package com.springprojects.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.Department;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.DepartmentService;
import com.springprojects.service.UserService;

public class Initializer {
	
	public Initializer(AuthorityService authorityService, UserService userService, AttachmentService attachmentService,
			DepartmentService departmentService, PasswordEncoder encoder) {
		// TODO Auto-generated constructor stub

		authorityService.create(new Authority(System.nanoTime(), "ROLE_ADMIN"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_QA_MANAGER"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_QA_COORDINATOR"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_STAFF"));
		authorityService.create(new Authority(System.nanoTime(), "ROLE_STUDENT"));
		
		Department department = new Department();
		department.setDepartmentId(System.currentTimeMillis());
		department.setDepartmentName("Admin");
		departmentService.saveOrUpdate(department);
		
		department.setDepartmentId(System.currentTimeMillis());
		department.setDepartmentName("Archiology");
		departmentService.saveOrUpdate(department);
		
		department.setDepartmentId(System.currentTimeMillis());
		department.setDepartmentName("QA");
		departmentService.saveOrUpdate(department);
		
		department.setDepartmentId(System.currentTimeMillis());
		department.setDepartmentName("Computer Science");
		departmentService.saveOrUpdate(department);
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Admin");
		userEntity.setEmail("teamg5.bit@gmail.com");
		userEntity.setDepartment("Admin");
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

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// ############################ Insert Student manager data

		// Student 1

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Mainul Hasan");
		userEntity.setEmail("mainuls18@gmail.com");
		userEntity.setDepartment("Computer Science");
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

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// Student 2

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Sazzad Hossain");
		userEntity.setEmail("sazzadhossainsakib@gmail.com");
		userEntity.setDepartment("Computer Science");
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

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// User 3

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Tanveer Rahman");
		userEntity.setEmail("tanveershuvos@gmail.com");
		userEntity.setDepartment("Archiology");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_QA_COORDINATOR"));
		authorities.add(authorityService.findByRoleName("ROLE_STAFF"));
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

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// Student 4

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Tanjina Akter Tamanna");
		userEntity.setEmail("tanjina523@gmail.com");
		userEntity.setDepartment("Archiology");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_STUDENT"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("tanjina");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Tanjina Image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// QA Manager

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("QA Manager");
		userEntity.setEmail("qa.manager.bit37@gmail.com");
		userEntity.setDepartment("QA");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_QA_MANAGER"));
		authorities.add(authorityService.findByRoleName("ROLE_STAFF"));
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

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

		// QA Coordinator

		userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("QA Coordinator");
		userEntity.setEmail("comp.sci.qa.coordinator@gmail.com");
		userEntity.setDepartment("Computer Science");
		authorities = new HashSet<>();
		authorities.add(authorityService.findByRoleName("ROLE_QA_COORDINATOR"));
		authorities.add(authorityService.findByRoleName("ROLE_STAFF"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword(encoder.encode("secret"));
		userEntity.setUsername("comp_sci_qa_coordinator");

		userImage = new Attachment();
		attachmentId = System.nanoTime();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("qa coordinator image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		if (!userService.existsWithUsername(userEntity.getUsername())) {
			attachmentService.save(userImage);
			userEntity.setUserImage(userImage);
			userService.createUser(userEntity);
		}

	}
}
