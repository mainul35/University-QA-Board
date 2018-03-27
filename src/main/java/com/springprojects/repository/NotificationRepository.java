package com.springprojects.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Notification;
import com.springprojects.entity.UserEntity;
@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByNotifyTo(UserEntity userEntity);

	List<Notification> findFirs10ByNotifyTo(UserEntity userEntity);

	void deleteLast50ByNotifyTo(UserEntity notifyTo);
	
}
