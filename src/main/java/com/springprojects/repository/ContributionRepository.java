package com.springprojects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Contribution;
import com.springprojects.entity.UserEntity;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long>{

	List<Contribution> findAllByContributor(UserEntity userEntity);

}
