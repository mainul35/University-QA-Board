package com.springprojects.service;

import com.springprojects.entity.Authority;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    Logger logger =Logger.getLogger(UserService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username.split("@")[0]);
        logger.info(userEntity.toString());
        return userEntity;
    }

    public UserEntity getUserById(Long id){
        return userRepository.findOne(id);
    }

    public boolean createUser(UserEntity user, boolean isTeacher){
        if(!existsWithEmail(user.getEmail()) || !existsWithUsername(user.getUsername())) {
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername(user.getEmail().split("@")[0]);
            Set<Authority> authorities = new HashSet();

            authorities.add(isTeacher==true?
                    authorityService.findByRoleName("ROLE_POTENTIAL_TEACHER")==null?
                            authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_POTENTIAL_TEACHER"))
                            :authorityService.findByRoleName("ROLE_POTENTIAL_TEACHER")
                    :authorityService.findByRoleName("ROLE_STUDENT")==null?
                            authorityService.create(new Authority(System.currentTimeMillis(),"ROLE_STUDENT"))
                            :authorityService.findByRoleName("ROLE_STUDENT"));
//            authorities.add(authorityService.create(new Authority(System.currentTimeMillis(), "ROLE_ADMIN")));
            user.setAuthorities(authorities);
            userRepository.save(user);
            logger.info(user.toString());
            return true;
        }
        return false;
    }

    public void createUser(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public boolean existsWithUsername(String username){
        if(userRepository.findByUsername(username) instanceof UserEntity){
            return true;
        }else{
            return false;
        }
    }

    public boolean existsWithEmail(String email){
        if(userRepository.findByUsername(email) instanceof UserEntity){
            return true;
        }else{
            return false;
        }
    }

    public List<UserEntity> usersWithRole(String role){
        return userRepository.findByAuthority(role);
    }
}