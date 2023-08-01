package com.mainul35.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainul35.entity.Comment;
import com.mainul35.entity.Idea;
import com.mainul35.entity.UserEntity;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	Comment findByIdeaAndCommentedUser(Idea idea, UserEntity userByUsername);

	List<Comment> findAllByIdea(Idea idea);
}
