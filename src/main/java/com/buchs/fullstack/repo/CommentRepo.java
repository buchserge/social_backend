package com.buchs.fullstack.repo;

import com.buchs.fullstack.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.message.id=:id")
    Page<Comment> findAllCommentsByMessage(Long id, Pageable pageable);
}
