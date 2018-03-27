package com.springprojects.repository;

import com.springprojects.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    
    UserEntity findByEmail(String email);

    @Query(value = "SELECT * FROM tbl_user u, tbl_authority a, user_authority au WHERE u.user_uuid = au.user_id AND a.role_uuid = au.role_id AND a.authority= ?1", nativeQuery = true)
    List<UserEntity> findByAuthority(String authority);
    
    List<UserEntity> findByDepartment(String department);
}
