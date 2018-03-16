package com.springprojects.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Activity;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.ActivityRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	public void saveOrUpdate(Activity activity) {
		if (activityRepository.findOne(activity.getId()) != null) {
			activityRepository.update(activity.getLastActivityDateTime(), activity.getId());
		} else {
			activityRepository.save(activity);
		}
	}

	public Activity findActivityById(Long id) {
		return activityRepository.getOne(id);
	}

	public String getLastActivityDifferenceOfUser(UserEntity userEntity) {
		if(null==activityRepository.findOne(userEntity.getId())) {
			return "None";
		}
		Activity activity = activityRepository.findOne(userEntity.getId());

		Date firstDate = new Date(activity.getLastActivityDateTime().getTime());
		Date secondDate = new Date(System.currentTimeMillis());

		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

		if (diff<60) {
			return diff +"m";
		}else if(diff>60 && diff<1440) {
			return diff/60 +"h";
		}else if(diff>1439 && diff<10080) {
			return diff/1440 +"d";
		}else if(diff>10079 && diff<525600) {
			return diff/10079 +"w";
		}else {
			return diff/525600 +"y";
		}
	}

	public List<Activity> findWeeklyInactiveUsers() {
		List<Activity> activities = activityRepository.findAll();
		activities.forEach(activity -> {
			Date firstDate = new Date(activity.getLastActivityDateTime().getTime());
			Date secondDate = new Date(System.currentTimeMillis());

			long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			if (diff < 7) {
				activities.remove(activity);
			}
		});
		return activities;
	}

}
