package com.springprojects.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Comment;
import com.springprojects.entity.Idea;
import com.springprojects.entity.UserEntity;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	Comment findByIdeaAndCommentedUser(Idea idea, UserEntity userByUsername);

	List<Comment> findAllByIdea(Idea idea);
}
