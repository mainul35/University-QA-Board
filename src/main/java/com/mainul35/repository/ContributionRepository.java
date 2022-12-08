package com.mainul35.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Contribution;
import com.mainul35.entity.UserEntity;

public interface ContributionRepository extends JpaRepository<Contribution, Long>{

	List<Contribution> findAllByContributor(UserEntity userEntity);

}
