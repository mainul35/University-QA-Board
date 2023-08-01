package com.mainul35.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Idea;
import com.mainul35.entity.Reaction;
import com.mainul35.entity.UserEntity;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

	Reaction findByIdeaAndReactedUser(Idea idea, UserEntity userByUsername);

}
