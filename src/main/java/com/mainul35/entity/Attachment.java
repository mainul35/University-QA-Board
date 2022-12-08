package com.mainul35.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ewsd_attachment")
public class Attachment implements Serializable{

	@Id
	@Column(name="attachment_id", length = 20, nullable = false)
	private Long attachmentId;
	@Column(name="file_name", nullable = false)
	private String fileName;
	@Column(name="file_title", nullable = true)
	private String fileTitle;
	@Column(name="file_url", nullable = false)
	private String fileURL;
	@Column(name="file_type")
	private String fileType;
	
	public Long getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachmentId == null) ? 0 : attachmentId.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((fileTitle == null) ? 0 : fileTitle.hashCode());
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((fileURL == null) ? 0 : fileURL.hashCode());
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
		Attachment other = (Attachment) obj;
		if (attachmentId == null) {
			if (other.attachmentId != null)
				return false;
		} else if (!attachmentId.equals(other.attachmentId))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (fileTitle == null) {
			if (other.fileTitle != null)
				return false;
		} else if (!fileTitle.equals(other.fileTitle))
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (fileURL == null) {
			if (other.fileURL != null)
				return false;
		} else if (!fileURL.equals(other.fileURL))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Attachment [attachmentId=" + attachmentId + ", fileName=" + fileName + ", fileTitle=" + fileTitle
				+ ", fileURL=" + fileURL + ", fileType=" + fileType + "]";
	}

}
