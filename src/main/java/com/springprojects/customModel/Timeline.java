package com.springprojects.customModel;

import com.springprojects.entity.Idea;

public class Timeline implements Comparable<Timeline>{
	String time;
	Idea idea;
	int totalThumbUp = 0;
	int totalThumbDown = 0;
	int totalComments = 0;
	int totalSeenBy = 0;
	int reactionOfCurrentUser = 0;
	boolean tagExpired = false;
	public Timeline() {
		super();
	}
	public Timeline(String time, Idea idea) {
		super();
		this.time = time;
		this.idea = idea;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Idea getIdea() {
		return idea;
	}
	public void setIdea(Idea idea) {
		this.idea = idea;
	}
	public int getTotalThumbUp() {
		return totalThumbUp;
	}
	public void setTotalThumbUp(int totalThumbUp) {
		this.totalThumbUp = totalThumbUp;
	}
	public int getTotalThumbDown() {
		return totalThumbDown;
	}
	public void setTotalThumbDown(int totalThumbDown) {
		this.totalThumbDown = totalThumbDown;
	}
	public int getTotalComments() {
		return totalComments;
	}
	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}
	public int getTotalSeenBy() {
		return totalSeenBy;
	}
	public void setTotalSeenBy(int totalSeenBy) {
		this.totalSeenBy = totalSeenBy;
	}
	
	public boolean isTagExpired() {
		return tagExpired;
	}
	public void setTagExpired(boolean tagExpired) {
		this.tagExpired = tagExpired;
	}
	@Override
	public String toString() {
		return "Timeline [time=" + time + ", idea=" + idea + ", totalThumbUp=" + totalThumbUp + ", totalThumbDown="
				+ totalThumbDown + ", totalComments=" + totalComments + ", totalSeenBy=" + totalSeenBy
				+ ", reactionOfCurrentUser=" + reactionOfCurrentUser + ", tagExpired=" + tagExpired + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idea == null) ? 0 : idea.hashCode());
		result = prime * result + reactionOfCurrentUser;
		result = prime * result + (tagExpired ? 1231 : 1237);
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + totalComments;
		result = prime * result + totalSeenBy;
		result = prime * result + totalThumbDown;
		result = prime * result + totalThumbUp;
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
		Timeline other = (Timeline) obj;
		if (idea == null) {
			if (other.idea != null)
				return false;
		} else if (!idea.equals(other.idea))
			return false;
		if (reactionOfCurrentUser != other.reactionOfCurrentUser)
			return false;
		if (tagExpired != other.tagExpired)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (totalComments != other.totalComments)
			return false;
		if (totalSeenBy != other.totalSeenBy)
			return false;
		if (totalThumbDown != other.totalThumbDown)
			return false;
		if (totalThumbUp != other.totalThumbUp)
			return false;
		return true;
	}
	@Override
	public int compareTo(Timeline o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.idea.hashCode(), o.idea.hashCode());
	}
	public void setReactionOfCurrentUser(int reactionType) {
		// TODO Auto-generated method stub
		this.reactionOfCurrentUser = reactionType;
	}
	public int getReactionOfCurrentUser() {
		return reactionOfCurrentUser;
	}
	
	
}
