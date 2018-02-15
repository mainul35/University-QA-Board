package com.springprojects.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="idea", catalog="ewsd")
public class Idea {

	@Id
	@Column(name = "idea_id", length = 20, nullable = false)
	Long ideaId;
	@Column(name="idea_title", length=200, nullable = false)
	String ideaTitle;
	@Column(name = "idea_body", length=200, nullable = false)
	String ideaBody;
	@Column(name="author_email", length=200, nullable = false)
	String authorEmail;
	@Column(name="total_viws")
	Integer countViews;
	@Column
	Date date;
	@Column
	Time time;
	@OneToMany
	Set<Attachment> attachments = new HashSet<>();
	@OneToMany
	Set<Comment> comments = new HashSet<>();
	@OneToMany
	Set<Reaction> reactions = new HashSet<>();
	@OneToMany
	Set<Tag> tags = new HashSet<>();
	@OneToMany
	Set<UserEntity> seenBy;
	
	public Long getIdeaId() {
		return ideaId;
	}
	public void setIdeaId(Long ideaId) {
		this.ideaId = ideaId;
	}
	public String getIdeaTitle() {
		return ideaTitle;
	}
	public void setIdeaTitle(String ideaTitle) {
		this.ideaTitle = ideaTitle;
	}
	public String getIdeaBody() {
		return ideaBody;
	}
	public void setIdeaBody(String ideaBody) {
		this.ideaBody = ideaBody;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	public Set<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
	public Set<Comment> getComments() {
		return comments;
	}
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	public Set<Reaction> getReactions() {
		return reactions;
	}
	public void setReactions(Set<Reaction> reactions) {
		this.reactions = reactions;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public Integer getCountViews() {
		return countViews;
	}
	public void setCountViews(Integer countViews) {
		this.countViews = countViews;
	}
	
	public Set<UserEntity> getSeenBy() {
		return seenBy;
	}
	public void setSeenBy(Set<UserEntity> seenBy) {
		this.seenBy = seenBy;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
		result = prime * result + ((authorEmail == null) ? 0 : authorEmail.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((countViews == null) ? 0 : countViews.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((ideaBody == null) ? 0 : ideaBody.hashCode());
		result = prime * result + ((ideaId == null) ? 0 : ideaId.hashCode());
		result = prime * result + ((ideaTitle == null) ? 0 : ideaTitle.hashCode());
		result = prime * result + ((reactions == null) ? 0 : reactions.hashCode());
		result = prime * result + ((seenBy == null) ? 0 : seenBy.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Idea other = (Idea) obj;
		if (attachments == null) {
			if (other.attachments != null)
				return false;
		} else if (!attachments.equals(other.attachments))
			return false;
		if (authorEmail == null) {
			if (other.authorEmail != null)
				return false;
		} else if (!authorEmail.equals(other.authorEmail))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (countViews == null) {
			if (other.countViews != null)
				return false;
		} else if (!countViews.equals(other.countViews))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (ideaBody == null) {
			if (other.ideaBody != null)
				return false;
		} else if (!ideaBody.equals(other.ideaBody))
			return false;
		if (ideaId == null) {
			if (other.ideaId != null)
				return false;
		} else if (!ideaId.equals(other.ideaId))
			return false;
		if (ideaTitle == null) {
			if (other.ideaTitle != null)
				return false;
		} else if (!ideaTitle.equals(other.ideaTitle))
			return false;
		if (reactions == null) {
			if (other.reactions != null)
				return false;
		} else if (!reactions.equals(other.reactions))
			return false;
		if (seenBy == null) {
			if (other.seenBy != null)
				return false;
		} else if (!seenBy.equals(other.seenBy))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Idea [ideaId=" + ideaId + ", ideaTitle=" + ideaTitle + ", ideaBody=" + ideaBody + ", authorEmail="
				+ authorEmail + ", countViews=" + countViews + ", date=" + date + ", time=" + time + ", attachments="
				+ attachments + ", comments=" + comments + ", reactions=" + reactions + ", tags=" + tags + ", seenBy="
				+ seenBy + "]";
	}
		
}
