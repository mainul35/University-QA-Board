package com.springprojects.service;

import com.springprojects.config.Mailer;
import com.springprojects.entity.Authority;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthorityService authorityService;

	Logger logger = Logger.getLogger(UserService.class.getName());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = (username.contains("@")) ? userRepository.findByEmail(username)
				: userRepository.findByUsername(username);
		logger.info(userEntity.toString());
		return userEntity;
	}

	public UserEntity getUserById(Long id) {
		return userRepository.findOne(id);
	}

	public UserEntity getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean createUser(UserEntity user, boolean isTeacher) {
		if (!existsWithEmail(user.getEmail()) || !existsWithUsername(user.getUsername())) {
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUsername(null);
			Set<Authority> authorities = new HashSet();

			// authorities.add(isTeacher==true?
			// authorityService.findByRoleName("ROLE_POTENTIAL_TEACHER")==null?
			// authorityService.create(new Authority(System.currentTimeMillis(),
			// "ROLE_POTENTIAL_TEACHER"))
			// :authorityService.findByRoleName("ROLE_POTENTIAL_TEACHER")
			// :authorityService.findByRoleName("ROLE_STUDENT")==null?
			// authorityService.create(new
			// Authority(System.currentTimeMillis(),"ROLE_STUDENT"))
			// :authorityService.findByRoleName("ROLE_STUDENT"));
			// authorities.add(authorityService.create(new
			// Authority(System.currentTimeMillis(), "ROLE_ADMIN")));
			user.setAuthorities(authorities);
			userRepository.save(user);
			logger.info(user.toString());
			return true;
		}
		return false;
	}

	public void createUser(UserEntity userEntity) {
		if(userRepository.findOne(userEntity.getId())==null && existsWithEmail(userEntity.getEmail())==false && existsWithUsername(userEntity.getUsername())==false)
			userRepository.save(userEntity);
	}

	public void updateUser(UserEntity userEntity) {
			userRepository.save(userEntity);
	}
	
	public boolean existsWithUsername(String username) {
		if (userRepository.findByUsername(username) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean existsWithEmail(String email) {
		if (userRepository.findByEmail(email) != null) {
			return true;
		} else {
			return false;
		}
	}

	public List<UserEntity> usersWithRole(String role) {
		return userRepository.findByAuthority(role);
	}

	public void sendPasswordResetLink(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		System.out.println(userEntity.toString());
		InetAddress IP = null;
		try {
			IP = Inet4Address.getLocalHost();
			System.out.println("IP of my system is := " + IP.getHostAddress());
			Mailer.sendMail(email, "DoNotReply", "Here is the password reset link for you.\n"
					+ " Please click to reset password. " + "\n http://" + IP.getHostAddress() + ":8080/ewsd/update-password?id="+userEntity.getUsername()+"&adp="+System.currentTimeMillis());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}