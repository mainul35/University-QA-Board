package com.springprojects.entity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="comment", catalog="ewsd")
public class Comment {

	@Id
	@Column(name="comment_Id", length=20, nullable=false)
	Long commentId;
	@Column(name="comment_body", nullable=false)
	String commentBody;
	@OneToOne(optional=false)
	@JoinColumn(name="commented_user_Id", unique = true, nullable = false, updatable = false)
	UserEntity commentedUser;
	@Column(name="anonymous")
	boolean isAnonymous;
	@Column
	Timestamp commentDateTime;
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public String getCommentBody() {
		return commentBody;
	}
	public void setCommentBody(String commentBody) {
		this.commentBody = commentBody;
	}
	public UserEntity getCommentedUser() {
		return commentedUser;
	}
	public void setCommentedUser(UserEntity commentedUser) {
		this.commentedUser = commentedUser;
	}
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	
	public Timestamp getCommentDateTime() {
		return commentDateTime;
	}
	public void setCommentDateTime(Timestamp commentDateTime) {
		this.commentDateTime = commentDateTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentBody == null) ? 0 : commentBody.hashCode());
		result = prime * result + ((commentDateTime == null) ? 0 : commentDateTime.hashCode());
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
		result = prime * result + ((commentedUser == null) ? 0 : commentedUser.hashCode());
		result = prime * result + (isAnonymous ? 1231 : 1237);
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
		Comment other = (Comment) obj;
		if (commentBody == null) {
			if (other.commentBody != null)
				return false;
		} else if (!commentBody.equals(other.commentBody))
			return false;
		if (commentDateTime == null) {
			if (other.commentDateTime != null)
				return false;
		} else if (!commentDateTime.equals(other.commentDateTime))
			return false;
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
		if (commentedUser == null) {
			if (other.commentedUser != null)
				return false;
		} else if (!commentedUser.equals(other.commentedUser))
			return false;
		if (isAnonymous != other.isAnonymous)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", commentBody=" + commentBody + ", commentedUser=" + commentedUser
				+ ", isAnonymous=" + isAnonymous + ", commentDateTime=" + commentDateTime + "]";
	}	
}
