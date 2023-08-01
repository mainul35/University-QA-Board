package com.mainul35.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Batch;

@Transactional
public interface BatchRepository extends JpaRepository<Batch, Long> {

	Batch findByAcademicYear(String academicYear);
	
	Batch findByBatchName(String batchName);
}
