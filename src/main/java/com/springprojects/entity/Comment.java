package com.springprojects.entity;

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
	UserEntity reactedUser;
	@Column(name="anonymous")
	boolean isAnonymous;
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
	public UserEntity getReactedUser() {
		return reactedUser;
	}
	public void setReactedUser(UserEntity reactedUser) {
		this.reactedUser = reactedUser;
	}
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentBody == null) ? 0 : commentBody.hashCode());
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
		result = prime * result + (isAnonymous ? 1231 : 1237);
		result = prime * result + ((reactedUser == null) ? 0 : reactedUser.hashCode());
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
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
		if (isAnonymous != other.isAnonymous)
			return false;
		if (reactedUser == null) {
			if (other.reactedUser != null)
				return false;
		} else if (!reactedUser.equals(other.reactedUser))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", commentBody=" + commentBody + ", reactedUser=" + reactedUser
				+ ", isAnonymous=" + isAnonymous + "]";
	}	
}
