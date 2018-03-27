package com.springprojects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Idea;
import com.springprojects.entity.Reaction;
import com.springprojects.entity.UserEntity;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

	Reaction findByIdeaAndReactedUser(Idea idea, UserEntity userByUsername);

}
