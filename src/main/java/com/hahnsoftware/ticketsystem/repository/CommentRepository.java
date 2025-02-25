package com.hahnsoftware.ticketsystem.repository;

import com.hahnsoftware.ticketsystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}