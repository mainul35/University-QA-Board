package com.mainul35.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "ewsd_idea")
//@EntityListeners(AuditingEntityListener.class)
public class Idea implements Serializable {

	@Id
	@Column(name = "idea_id", length = 20, nullable = false)
	private Long ideaId;
	@Column(name = "idea_title", length = 200, nullable = false)
	private String ideaTitle;
	@Column(name = "idea_body", nullable = false, columnDefinition = "TEXT")
	private String ideaBody;
	@Column(name = "author_email", length = 200, nullable = false)
	private String authorEmail;
	@Column(name = "total_viws")
	private Integer countViews = 0;
	@Column
	private Timestamp publishingDate;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.ALL)
	private List<Comment> comments;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "idea", cascade = CascadeType.ALL)
	private Set<Reaction> reactions;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "idea_attachments")
	private Set<Attachment> attachments;
	@ManyToOne
	private Tag tag = new Tag();
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "idea_seen_by")
	private Set<String> seenBy = new HashSet<>();
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
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

	public Set<String> getSeenBy() {
		return seenBy;
	}

	public void setSeenBy(Set<String> seenBy) {
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
