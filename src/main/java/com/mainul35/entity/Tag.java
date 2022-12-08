package com.mainul35.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ewsd_tag")
public class Tag  implements Serializable{
	@Id
	@Column(nullable=false, length=20)
	private Long tagId;
	
	@Column(name="tagName", length=50, nullable=false)
	private String tagName;

	@Column
	private Timestamp openingDate;

	@Column
	private Timestamp closingDate;
	
	@Column
	private Timestamp finalClosingDate;
	
	@Column(name="departments")
	private String departments;
	
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public Timestamp getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Timestamp openingDate) {
		this.openingDate = openingDate;
	}

	public Timestamp getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Timestamp closingDate) {
		this.closingDate = closingDate;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Timestamp getFinalClosingDate() {
		return finalClosingDate;
	}

	public void setFinalClosingDate(Timestamp finalClosingDate) {
		this.finalClosingDate = finalClosingDate;
	}


	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closingDate == null) ? 0 : closingDate.hashCode());
		result = prime * result + ((departments == null) ? 0 : departments.hashCode());
		result = prime * result + ((finalClosingDate == null) ? 0 : finalClosingDate.hashCode());
		result = prime * result + ((openingDate == null) ? 0 : openingDate.hashCode());
		result = prime * result + ((tagId == null) ? 0 : tagId.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
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
		Tag other = (Tag) obj;
		if (closingDate == null) {
			if (other.closingDate != null)
				return false;
		} else if (!closingDate.equals(other.closingDate))
			return false;
		if (departments == null) {
			if (other.departments != null)
				return false;
		} else if (!departments.equals(other.departments))
			return false;
		if (finalClosingDate == null) {
			if (other.finalClosingDate != null)
				return false;
		} else if (!finalClosingDate.equals(other.finalClosingDate))
			return false;
		if (openingDate == null) {
			if (other.openingDate != null)
				return false;
		} else if (!openingDate.equals(other.openingDate))
			return false;
		if (tagId == null) {
			if (other.tagId != null)
				return false;
		} else if (!tagId.equals(other.tagId))
			return false;
		if (tagName == null) {
			if (other.tagName != null)
				return false;
		} else if (!tagName.equals(other.tagName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", tagName=" + tagName + ", openingDate=" + openingDate + ", closingDate="
				+ closingDate + ", finalClosingDate=" + finalClosingDate + ", departments=" + departments + "]";
	}
	
	
}
