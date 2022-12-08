package com.mainul35.service;

import com.mainul35.entity.Authority;
import com.mainul35.repository.AuthorityRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority create(Authority authority){
    	
    	if(exists(authority.getAuthority())) {
    		return null;
    	}else {
    		authorityRepository.save(authority);
            return authority;
    	}    		
    }

    public Authority findByRoleName(String roleName){
        return authorityRepository.findByAuthority(roleName);
    }
    
    public boolean exists(String role) {
    	if(findByRoleName(role)!=null) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public List<Authority> listAllAuthorities(){
    	return authorityRepository.findAll();
    }
}
