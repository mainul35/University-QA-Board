package com.mainul35.repository;

import com.mainul35.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{
    Authority findByAuthority(String authority);
}
