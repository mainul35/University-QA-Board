package com.springprojects.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Activity;
import com.springprojects.entity.UserEntity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>{
	@Transactional
	@Modifying
	@Query("update Activity a set a.lastActivityDateTime = ?1 where a.id = ?2")
	void update(Timestamp dateTime, Long userId);

}
