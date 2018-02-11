package com.springprojects.service;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.springprojects.config.application.RootConfig;
import com.springprojects.config.application.ServletConfig;
import com.springprojects.config.persistence.MySqlConfig;
import com.springprojects.entity.UserEntity;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RootConfig.class, ServletConfig.class, MySqlConfig.class}, loader = AnnotationConfigContextLoader.class)
class UserServiceTest {
	Logger logger = Logger.getLogger(UserServiceTest.class.getName());
	
	@Autowired
	UserService userService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLoadUserByUsername() {
		System.out.println(userService);
		UserEntity entity = (UserEntity)userService.loadUserByUsername("admin");
		logger.info(entity.toString());
		fail("Not yet implemented");
	}

	@Test
	void testGetUserById() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateUserUserEntityBoolean() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateUserUserEntity() {
		fail("Not yet implemented");
	}

	@Test
	void testExistsWithUsername() {
		fail("Not yet implemented");
	}

	@Test
	void testExistsWithEmail() {
		fail("Not yet implemented");
	}

	@Test
	void testUsersWithRole() {
		fail("Not yet implemented");
	}

}
