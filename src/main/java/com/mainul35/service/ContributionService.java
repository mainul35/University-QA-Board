package com.mainul35.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainul35.entity.Contribution;
import com.mainul35.entity.UserEntity;
import com.mainul35.repository.ContributionRepository;

@Service
public class ContributionService {

	@Autowired
	private ContributionRepository contributionRepository;
	
	public void save(Contribution contribution) {
		contributionRepository.save(contribution);
	}
	
	public Contribution findContributionById(Long id) {
		return contributionRepository.getOne(id);
	}

	public List<Contribution> findContributionsByUser(UserEntity userEntity) {
		return contributionRepository.findAllByContributor(userEntity);
	}
}
