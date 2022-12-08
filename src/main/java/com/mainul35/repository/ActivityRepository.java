package com.mainul35.repository;

import java.sql.Timestamp;

import com.mainul35.repository.util.HibernateRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>, HibernateRepository<Activity> {
	@Transactional
	@Modifying
	@Query("update Activity a set a.lastActivityDateTime = ?1 where a.id = ?2")
	void update(Timestamp dateTime, Long userId);

}
