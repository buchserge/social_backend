package com.buchs.fullstack.service;

import com.buchs.fullstack.entity.Comment;
import com.buchs.fullstack.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    public Page<Comment> getAllCommentsByMessage(Long id, Pageable pageable) {
        return commentRepo.findAllCommentsByMessage(id, pageable);
    }


    public Comment createComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public void deleteCommentById(Long id) {
        commentRepo.deleteById(id);
    }
}
