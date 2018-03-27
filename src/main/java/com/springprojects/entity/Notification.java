package com.springprojects.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Notification {
	@Id
	@Column(name="notification_id")
	private Long notificationId;
	@Column(name="notification_type")
	private String notificationType;
	@Column(name="notification_msg")
	private String notificationMsg;
	@Column(name="notification_url")
	private String notificationUrl;
	@ManyToOne
	private UserEntity notifyTo;
	@OneToOne
	private UserEntity notificationFrom;
	@Column(name="notification_seen")
	private String seen;
	@Column(name="notifiable_departments")
	private String notifiableDepartments;
	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getNotificationMsg() {
		return notificationMsg;
	}
	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}
	public String getNotificationUrl() {
		return notificationUrl;
	}
	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}
	public UserEntity getNotifyTo() {
		return notifyTo;
	}
	public void setNotifyTo(UserEntity notifyTo) {
		this.notifyTo = notifyTo;
	}
	public UserEntity getNotificationFrom() {
		return notificationFrom;
	}
	public void setNotificationFrom(UserEntity notificationFrom) {
		this.notificationFrom = notificationFrom;
	}
	public String getNotifiableDepartments() {
		return notifiableDepartments;
	}
	public void setNotifiableDepartments(String notifiableDepartments) {
		this.notifiableDepartments = notifiableDepartments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notifiableDepartments == null) ? 0 : notifiableDepartments.hashCode());
		result = prime * result + ((notificationFrom == null) ? 0 : notificationFrom.hashCode());
		result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
		result = prime * result + ((notificationMsg == null) ? 0 : notificationMsg.hashCode());
		result = prime * result + ((notificationType == null) ? 0 : notificationType.hashCode());
		result = prime * result + ((notificationUrl == null) ? 0 : notificationUrl.hashCode());
		result = prime * result + ((notifyTo == null) ? 0 : notifyTo.hashCode());
		result = prime * result + ((seen == null) ? 0 : seen.hashCode());
		return result;
	}
	public String getSeen() {
		return seen;
	}
	public void setSeen(String seen) {
		this.seen = seen;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (notifiableDepartments == null) {
			if (other.notifiableDepartments != null)
				return false;
		} else if (!notifiableDepartments.equals(other.notifiableDepartments))
			return false;
		if (notificationFrom == null) {
			if (other.notificationFrom != null)
				return false;
		} else if (!notificationFrom.equals(other.notificationFrom))
			return false;
		if (notificationId == null) {
			if (other.notificationId != null)
				return false;
		} else if (!notificationId.equals(other.notificationId))
			return false;
		if (notificationMsg == null) {
			if (other.notificationMsg != null)
				return false;
		} else if (!notificationMsg.equals(other.notificationMsg))
			return false;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		if (notificationUrl == null) {
			if (other.notificationUrl != null)
				return false;
		} else if (!notificationUrl.equals(other.notificationUrl))
			return false;
		if (notifyTo == null) {
			if (other.notifyTo != null)
				return false;
		} else if (!notifyTo.equals(other.notifyTo))
			return false;
		if (seen == null) {
			if (other.seen != null)
				return false;
		} else if (!seen.equals(other.seen))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Notification [notificationId=" + notificationId + ", notificationType=" + notificationType
				+ ", notificationMsg=" + notificationMsg + ", notificationUrl=" + notificationUrl + ", notifyTo="
				+ notifyTo + ", notificationFrom=" + notificationFrom + ", seen=" + seen + ", notifiableDepartments="
				+ notifiableDepartments + "]";
	}
	
}
