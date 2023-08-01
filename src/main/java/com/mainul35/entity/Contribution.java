package com.mainul35.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ewsd_contribution")
public class Contribution {

	@Id
	@Column(name="contribution_id")
	private Long contributionId;
	@ManyToOne
	private UserEntity contributor;

	public Contribution() {
		super();
	}
	
	public Contribution(Long contributionId, UserEntity contributor) {
		// TODO Auto-generated constructor stub
		this.contributionId = contributionId;
		this.contributor = contributor;
	}
	public Long getContributionId() {
		return contributionId;
	}
	public void setContributionId(Long contributionId) {
		this.contributionId = contributionId;
	}
	public UserEntity getContributor() {
		return contributor;
	}
	public void setContributor(UserEntity contributor) {
		this.contributor = contributor;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contributionId == null) ? 0 : contributionId.hashCode());
		result = prime * result + ((contributor == null) ? 0 : contributor.hashCode());
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
		Contribution other = (Contribution) obj;
		if (contributionId == null) {
			if (other.contributionId != null)
				return false;
		} else if (!contributionId.equals(other.contributionId))
			return false;
		if (contributor == null) {
			if (other.contributor != null)
				return false;
		} else if (!contributor.equals(other.contributor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Contribution [contributionId=" + contributionId + ", contributor=" + contributor + "]";
	}
}
