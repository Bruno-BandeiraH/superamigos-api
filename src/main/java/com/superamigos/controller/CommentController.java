package com.superamigos.controller;

import com.superamigos.domain.comment.Comment;
import com.superamigos.domain.comment.CommentService;
import com.superamigos.domain.comment.dto.CommentCreationData;
import com.superamigos.domain.comment.dto.CommentDetailsData;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid CommentCreationData data, Long postId,
                                 Long userId, UriComponentsBuilder uriBuilder) {
        Comment comment = commentService.create(data, userId, postId);
        var uri = uriBuilder.path("/comments/{id}").buildAndExpand(comment.getId()).toUri();
        return ResponseEntity.created(uri).body(new CommentDetailsData(comment));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deleteById(Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity findAllByPostId(Long postId) {
        List<CommentDetailsData> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }
}
