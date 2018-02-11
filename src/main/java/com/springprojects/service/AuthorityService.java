package com.springprojects.service;

import com.springprojects.entity.Authority;
import com.springprojects.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;

    public Authority create(Authority authority){
        authorityRepository.save(authority);
        return authority;
    }

    public Authority findByRoleName(String roleName){
        return authorityRepository.findByAuthority(roleName);
    }
}
