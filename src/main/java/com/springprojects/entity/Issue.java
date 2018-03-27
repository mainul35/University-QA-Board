package com.springprojects.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@Entity
public class Issue {

	@Id
	@Column(name="issue_id")
	private Long issueId;
	@Column(name="issue_name")
	private String issueName;
	@Lob
	@Column(name="issue_description")
	private String issueDescription;
	@Column(name="issue_raised_date")
	private Timestamp issueRaisedDate;
	@Column(name = "issue_closure_date")
	private Timestamp issueClosureDate;
	@Column(name="issue_status")
	private String issueStatus;
	@OneToOne
	private UserEntity issueSubmittedTo;
	@OneToOne
	private UserEntity issueSubmittedBy;
	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	public String getIssueName() {
		return issueName;
	}
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
	public String getIssueDescription() {
		return issueDescription;
	}
	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	public Timestamp getIssueRaisedDate() {
		return issueRaisedDate;
	}
	public void setIssueRaisedDate(Timestamp issueRaisedDate) {
		this.issueRaisedDate = issueRaisedDate;
	}
	public Timestamp getIssueClosureDate() {
		return issueClosureDate;
	}
	public void setIssueClosureDate(Timestamp issueClosureDate) {
		this.issueClosureDate = issueClosureDate;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public UserEntity getIssueSubmittedTo() {
		return issueSubmittedTo;
	}
	public void setIssueSubmittedTo(UserEntity issueSubmittedTo) {
		this.issueSubmittedTo = issueSubmittedTo;
	}
	public UserEntity getIssueSubmittedBy() {
		return issueSubmittedBy;
	}
	public void setIssueSubmittedBy(UserEntity issueSubmittedBy) {
		this.issueSubmittedBy = issueSubmittedBy;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((issueClosureDate == null) ? 0 : issueClosureDate.hashCode());
		result = prime * result + ((issueDescription == null) ? 0 : issueDescription.hashCode());
		result = prime * result + ((issueId == null) ? 0 : issueId.hashCode());
		result = prime * result + ((issueName == null) ? 0 : issueName.hashCode());
		result = prime * result + ((issueRaisedDate == null) ? 0 : issueRaisedDate.hashCode());
		result = prime * result + ((issueStatus == null) ? 0 : issueStatus.hashCode());
		result = prime * result + ((issueSubmittedBy == null) ? 0 : issueSubmittedBy.hashCode());
		result = prime * result + ((issueSubmittedTo == null) ? 0 : issueSubmittedTo.hashCode());
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
		Issue other = (Issue) obj;
		if (issueClosureDate == null) {
			if (other.issueClosureDate != null)
				return false;
		} else if (!issueClosureDate.equals(other.issueClosureDate))
			return false;
		if (issueDescription == null) {
			if (other.issueDescription != null)
				return false;
		} else if (!issueDescription.equals(other.issueDescription))
			return false;
		if (issueId == null) {
			if (other.issueId != null)
				return false;
		} else if (!issueId.equals(other.issueId))
			return false;
		if (issueName == null) {
			if (other.issueName != null)
				return false;
		} else if (!issueName.equals(other.issueName))
			return false;
		if (issueRaisedDate == null) {
			if (other.issueRaisedDate != null)
				return false;
		} else if (!issueRaisedDate.equals(other.issueRaisedDate))
			return false;
		if (issueStatus == null) {
			if (other.issueStatus != null)
				return false;
		} else if (!issueStatus.equals(other.issueStatus))
			return false;
		if (issueSubmittedBy == null) {
			if (other.issueSubmittedBy != null)
				return false;
		} else if (!issueSubmittedBy.equals(other.issueSubmittedBy))
			return false;
		if (issueSubmittedTo == null) {
			if (other.issueSubmittedTo != null)
				return false;
		} else if (!issueSubmittedTo.equals(other.issueSubmittedTo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Issue [issueId=" + issueId + ", issueName=" + issueName + ", issueDescription=" + issueDescription
				+ ", issueRaisedDate=" + issueRaisedDate + ", issueClosureDate=" + issueClosureDate + ", issueStatus="
				+ issueStatus + ", issueSubmittedTo=" + issueSubmittedTo + ", issueSubmittedBy=" + issueSubmittedBy
				+ "]";
	}
	
}
