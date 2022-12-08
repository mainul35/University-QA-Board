package com.mainul35.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ewsd_reaction")
public class Reaction  implements Serializable, Comparable<Reaction>{

	@Id
	@Column(name="reaction_Id", length=20, nullable=false)
	private Long reactionId;
	@OneToOne
	@JoinColumn(
			name="reacted_user_Id", 
			nullable = false, 
			updatable=false)
	private UserEntity reactedUser;
	@ManyToOne
	@JsonIgnore
	private Idea idea = new Idea();
	@Column(name="reaction_type", nullable = false, updatable = true)
	private Integer reactionType;
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

	public Idea getIdea() {
		return idea;
	}
	public void setIdea(Idea idea) {
		this.idea = idea;
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
		if (idea == null) {
			if (other.idea != null)
				return false;
		} else if (!idea.equals(other.idea))
			return false;
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
		return "Reaction [reactionId=" + reactionId + ", reactedUser=" + reactedUser 
				+ ", reactionType=" + reactionType + "]";
	}
	
	@Override
	public int compareTo(Reaction o) {
		return Integer.compare(this.hashCode(), o.hashCode());
	}
	
	
}
