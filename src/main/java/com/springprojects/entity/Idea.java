package com.springprojects.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "idea", catalog = "ewsd")
public class Idea implements Serializable {

	@Id
	@Column(name = "idea_id", length = 20, nullable = false)
	Long ideaId;
	@Column(name = "idea_title", length = 200, nullable = false)
	String ideaTitle;
	@Column(name = "idea_body", length = 200, nullable = false)
	String ideaBody;
	@Column(name = "author_email", length = 200, nullable = false)
	String authorEmail;
	@Column(name = "total_viws")
	Integer countViews;
	@Column
	Timestamp publishingDate;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.ALL)
	Set<Comment> comments = new HashSet<>(0);
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.ALL)
	Set<Reaction> reactions = new HashSet<>(0);
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "idea_attachments")
	Set<Attachment> attachments = new HashSet<>(0);
	@ManyToOne
	Tag tag = new Tag();
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "idea_seen_by", joinColumns = {
			@JoinColumn(name = "idea_id", referencedColumnName = "idea_id") }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "user_uuid") })
	Set<UserEntity> seenBy = new HashSet<>(0);

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

	public Integer getCountViews() {
		return countViews;
	}

	public void setCountViews(Integer countViews) {
		this.countViews = countViews;
	}

	public Timestamp getPublishingDate() {
		return publishingDate;
	}

	public void setPublishingDate(Timestamp publishingDate) {
		this.publishingDate = publishingDate;
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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Set<UserEntity> getSeenBy() {
		return seenBy;
	}

	public void setSeenBy(Set<UserEntity> seenBy) {
		this.seenBy = seenBy;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
		result = prime * result + ((authorEmail == null) ? 0 : authorEmail.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((countViews == null) ? 0 : countViews.hashCode());
		result = prime * result + ((ideaBody == null) ? 0 : ideaBody.hashCode());
		result = prime * result + ((ideaId == null) ? 0 : ideaId.hashCode());
		result = prime * result + ((ideaTitle == null) ? 0 : ideaTitle.hashCode());
		result = prime * result + ((publishingDate == null) ? 0 : publishingDate.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		if (publishingDate == null) {
			if (other.publishingDate != null)
				return false;
		} else if (!publishingDate.equals(other.publishingDate))
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
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Idea [ideaId=" + ideaId + ", ideaTitle=" + ideaTitle + ", ideaBody=" + ideaBody + ", authorEmail="
				+ authorEmail + ", countViews=" + countViews + ", publishingDate=" + publishingDate + ", attachments="
				+ attachments + ", tag=" + tag + ", seenBy=" + seenBy + "]";
	}

}
