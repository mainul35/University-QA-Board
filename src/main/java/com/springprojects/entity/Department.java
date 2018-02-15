package com.springprojects.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Department {
@Id
@Column(name="dept_id", length=20, nullable=false)
Long deptId;
@Column(name="dept_name", unique=true)
String deptName;
@OneToMany
Set<Batch> batchs;
public Long getDeptId() {
	return deptId;
}
public void setDeptId(Long deptId) {
	this.deptId = deptId;
}
public String getDeptName() {
	return deptName;
}
public void setDeptName(String deptName) {
	this.deptName = deptName;
}
public Set<Batch> getBatchs() {
	return batchs;
}
public void setBatchs(Set<Batch> batchs) {
	this.batchs = batchs;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((batchs == null) ? 0 : batchs.hashCode());
	result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
	result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
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
	Department other = (Department) obj;
	if (batchs == null) {
		if (other.batchs != null)
			return false;
	} else if (!batchs.equals(other.batchs))
		return false;
	if (deptId == null) {
		if (other.deptId != null)
			return false;
	} else if (!deptId.equals(other.deptId))
		return false;
	if (deptName == null) {
		if (other.deptName != null)
			return false;
	} else if (!deptName.equals(other.deptName))
		return false;
	return true;
}
@Override
public String toString() {
	return "Department [deptId=" + deptId + ", deptName=" + deptName + ", batchs=" + batchs + "]";
}

}
