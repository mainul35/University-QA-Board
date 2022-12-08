package com.mainul35.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainul35.entity.Notification;
import com.mainul35.entity.UserEntity;
import com.mainul35.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public void save(Notification notification) {
		// TODO Auto-generated method stub
		notificationRepository.save(notification);
		deleteIfLimitExceed(notification);
	}
	
	public List<Notification> findNotificationsByUser(UserEntity userEntity){
		return notificationRepository.findByNotifyTo(userEntity);
	}
	public int count(UserEntity userEntity) {
		return notificationRepository.findByNotifyTo(userEntity).size();
	}
	public void deleteIfLimitExceed(Notification notification) {
		if(count(notification.getNotifyTo())>500) {
			notificationRepository.deleteLast50ByNotifyTo(notification.getNotifyTo());
		}
	}

	public Notification findById(long id) {
		// TODO Auto-generated method stub
		return notificationRepository.findById(id).orElse(null);
	}

	public void update(Notification notificaiton) {
		// TODO Auto-generated method stub
		notificationRepository.save(notificaiton);
	}

	public void delete(Notification notification) {
		// TODO Auto-generated method stub
		notificationRepository.delete(notification);
	}
}
