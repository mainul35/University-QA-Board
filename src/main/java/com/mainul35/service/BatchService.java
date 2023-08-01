package com.mainul35.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainul35.entity.Batch;
import com.mainul35.repository.BatchRepository;

@Service
public class BatchService {

	@Autowired
	private BatchRepository batchRepository;
	
	public void save(Batch batch) {
		batchRepository.save(batch);
	}
	
	public List<Batch> findAllBatches(){
		return batchRepository.findAll();
	}
	
	public Batch findByBatchName(String batchName) {
		return batchRepository.findByBatchName(batchName);
	}
	
	public Batch findByAcademicYear(String academicYear) {
		return batchRepository.findByAcademicYear(academicYear);
	}
	
	public void delete(Batch batch) {
		batchRepository.delete(batch);
	}
}
