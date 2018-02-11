package com.springprojects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springprojects.entity.Attachment;
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}
