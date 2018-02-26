package com.springprojects.customModel;

import com.springprojects.entity.Idea;

public class Timeline implements Comparable<Timeline>{
	String time;
	Idea idea;
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idea == null) ? 0 : idea.hashCode());
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
		Timeline other = (Timeline) obj;
		if (idea == null) {
			if (other.idea != null)
				return false;
		} else if (!idea.equals(other.idea))
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
		return "Timeline [time=" + time + ", idea=" + idea + "]";
	}
	@Override
	public int compareTo(Timeline o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.idea.hashCode(), o.idea.hashCode());
	}
}
