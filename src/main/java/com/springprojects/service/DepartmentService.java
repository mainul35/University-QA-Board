package com.springprojects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springprojects.entity.Department;
import com.springprojects.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	public void save(Department department) {
		departmentRepository.save(department);
	}
	
	public List<Department> getAllDepartments(){
		return departmentRepository.findAll();
	}
	
	public Department findByDepartmentName(String departmentName) {
		return departmentRepository.findByDepartmentName(departmentName);
	}
	
	public Department findByDepartmentId(Long departmentId) {
		return departmentRepository.findById(departmentId).orElse(null);
	}

	public String saveOrUpdate(Department department) {
		// TODO Auto-generated method stub
		Department department2 = findByDepartmentName(department.getDepartmentName());
		if(department2 == null) {
			department2 = findByDepartmentId(department.getDepartmentId());
			if(department2 == null) {
				departmentRepository.save(department);
				return "saved";
			}else {
				department2.setDepartmentName(department.getDepartmentName());
				departmentRepository.save(department2);
				return "updated";
			}
		}else{
			return "updated";
		}
	}
}
