package com.springprojects.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Batch;

@Repository
@Transactional
public interface BatchRepository extends JpaRepository<Batch, Long> {

	Batch findByAcademicYear(String academicYear);
	
	Batch findByBatchName(String batchName);
}
