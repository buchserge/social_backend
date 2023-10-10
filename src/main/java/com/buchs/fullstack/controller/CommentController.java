package com.buchs.fullstack.controller;

import com.buchs.fullstack.entity.Comment;
import com.buchs.fullstack.entity.Message;
import com.buchs.fullstack.service.CommentService;
import com.buchs.fullstack.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/getComments/{messageId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<Comment>> fetchComments(@RequestParam(defaultValue = "0") int page, @PathVariable long messageId) {

        Page<Comment> comments = commentService.getAllCommentsByMessage(messageId, PageRequest.of(page, 5,Sort.by("id").descending()));
        System.out.println(comments.getContent());
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/createComment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void createComment(@RequestParam Long messageId, @RequestBody Comment comment) {

        Message msg = messageService.getMessageById(messageId);
        comment.setMessage(msg);
        Comment result = commentService.createComment(comment);



    }

    @DeleteMapping("/deleteComment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity deleteComment(@RequestParam Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }


}
