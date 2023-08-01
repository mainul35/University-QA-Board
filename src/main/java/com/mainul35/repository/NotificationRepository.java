package com.mainul35.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Notification;
import com.mainul35.entity.UserEntity;
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByNotifyTo(UserEntity userEntity);

	List<Notification> findFirs10ByNotifyTo(UserEntity userEntity);

	void deleteLast50ByNotifyTo(UserEntity notifyTo);
	
}
