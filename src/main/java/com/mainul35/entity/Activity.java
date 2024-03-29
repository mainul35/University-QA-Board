package com.mainul35.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ewsd_activity")
public class Activity {

	@Id
	@Column(name="activity_id")
	private Long id;
	@Column(name="last_activity_date_time")
	private Timestamp lastActivityDateTime = new Timestamp(System.currentTimeMillis());
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getLastActivityDateTime() {
		return lastActivityDateTime;
	}
	public void setLastActivityDateTime(Timestamp lastActivityDateTime) {
		this.lastActivityDateTime = lastActivityDateTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastActivityDateTime == null) ? 0 : lastActivityDateTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastActivityDateTime == null) {
			if (other.lastActivityDateTime != null)
				return false;
		} else if (!lastActivityDateTime.equals(other.lastActivityDateTime))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Activity [id=" + id + ", lastActivityDateTime=" + lastActivityDateTime + "]";
	}
	
}
