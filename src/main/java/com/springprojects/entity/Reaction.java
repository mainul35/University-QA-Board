package com.springprojects.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="reaction", schema="ewsd")
public class Reaction  implements Serializable{

	@Id
	@Column(name="reaction_Id", length=20, nullable=false)
	Long reactionId;
	@OneToOne(optional=false)
	@JoinColumn(name="reacted_user_Id", unique = true, nullable = false, updatable = false)
	UserEntity reactedUser;
	@Column(name="reaction_type", nullable = false, updatable = false)
	Integer reactionType;
	public Long getReactionId() {
		return reactionId;
	}
	public void setReactionId(Long reactionId) {
		this.reactionId = reactionId;
	}
	public UserEntity getReactedUser() {
		return reactedUser;
	}
	public void setReactedUser(UserEntity reactedUser) {
		this.reactedUser = reactedUser;
	}
	public Integer getReactionType() {
		return reactionType;
	}
	public void setReactionType(Integer reactionType) {
		this.reactionType = reactionType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reactedUser == null) ? 0 : reactedUser.hashCode());
		result = prime * result + ((reactionId == null) ? 0 : reactionId.hashCode());
		result = prime * result + ((reactionType == null) ? 0 : reactionType.hashCode());
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
		Reaction other = (Reaction) obj;
		if (reactedUser == null) {
			if (other.reactedUser != null)
				return false;
		} else if (!reactedUser.equals(other.reactedUser))
			return false;
		if (reactionId == null) {
			if (other.reactionId != null)
				return false;
		} else if (!reactionId.equals(other.reactionId))
			return false;
		if (reactionType == null) {
			if (other.reactionType != null)
				return false;
		} else if (!reactionType.equals(other.reactionType))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reaction [reactionId=" + reactionId + ", reactedUser=" + reactedUser + ", reactionType=" + reactionType
				+ "]";
	}
	
	
}
