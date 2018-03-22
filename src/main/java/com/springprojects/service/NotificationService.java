package com.springprojects.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Notification;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.NotificationRepository;

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
		return notificationRepository.findOne(id);
	}

	public void update(Notification notificaiton) {
		// TODO Auto-generated method stub
		notificationRepository.save(notificaiton);
	}
}
