package com.springprojects.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.springprojects.config.Mailer;
import com.springprojects.config.persistence.MySqlConfig;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.Authority;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.AttachmentRepository;
import com.springprojects.repository.AuthorityRepository;
import com.springprojects.repository.UserRepository;
import com.springprojects.service.AuthorityService;
import com.springprojects.service.UserService;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class UserServiceTest {
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	
	private Logger logger = Logger.getLogger(UserService.class.getName());

	@Test
	public void creteOrUpdate() {
		assertThat(userRepository.findByUsername("admin")).isNotNull();		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(System.nanoTime());
		userEntity.setName("Admin");
		userEntity.setEmail("teamg5.bit@gmail.com");
		userEntity.setDepartment("Admin");
		Set<Authority> authorities = new HashSet<>();
		authorities.add(authorityRepository.findByAuthority("ROLE_ADMIN"));
		userEntity.setAuthorities(authorities);
		userEntity.setEnabled(true);
		userEntity.setPassword("secret");
		userEntity.setUsername("admin");

		Attachment userImage = new Attachment();
		Long attachmentId = System.currentTimeMillis();
		userImage.setAttachmentId(attachmentId);
		userImage.setFileName(Long.toString(attachmentId));
		userImage.setFileTitle("Admin image");
		userImage.setFileURL("/resources/contents/lib/img/user1-128x128.jpg");

		if (!existsWithUsername(userEntity.getUsername())) {
			attachmentRepository.save(userImage);
			userEntity.setUserImage(userImage);
		}

		if(existsWithEmail(userEntity.getEmail()) || existsWithUsername(userEntity.getUsername())) {
			UserEntity userEntity2 = userRepository.findByUsername(userEntity.getUsername());
			userEntity.setId(userEntity2.getId());
			userRepository.save(userEntity);
		}else {
			userRepository.save(userEntity);
		}
		
		assertThat(userRepository.findByUsername("admin")).isExactlyInstanceOf(UserEntity.class);
	}

	@Test
	public void loadUserByUsername() throws UsernameNotFoundException {
		String username = "admin";
		UserEntity userEntity = (username.contains("@")) ? userRepository.findByEmail(username)
				: userRepository.findByUsername(username);
		assertThat(userEntity).isExactlyInstanceOf(UserEntity.class);
	}

	@Test
	public void getUserById() {
		long id = 10L;
		assertThat(userRepository.findOne(id)).isExactlyInstanceOf(UserEntity.class);
	}

	@Test
	public void getUserByUsername() {
		String username = "admin";
		assertThat(userRepository.findByUsername(username)).isExactlyInstanceOf(UserEntity.class);
	}

	@Test
	public void getUserByEmail() {
		String email = "teamg5.bit@gmail.com";
		assertThat(userRepository.findByEmail(email)).isExactlyInstanceOf(UserEntity.class);
	}
	
	@Test
	public void getAllUsers(){
		assertThat(userRepository.findAll()).asList();
	}

	@Test
	public void getUsersByDepartment(){
		String department = "Admin";
		assertThat(userRepository.findByDepartment(department)).asList();
	}

	private boolean existsWithUsername(String username) {
		if (userRepository.findByUsername(username) != null) {
			return true;
		} else {
			return false;
		}
	}

	private boolean existsWithEmail(String email) {
		if (userRepository.findByEmail(email) != null) {
			return true;
		} else {
			return false;
		}
	}

	
}
