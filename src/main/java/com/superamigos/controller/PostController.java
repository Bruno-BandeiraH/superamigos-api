package com.superamigos.controller;

import com.superamigos.domain.post.Post;
import com.superamigos.domain.post.PostService;
import com.superamigos.domain.post.dto.PostCreationData;
import com.superamigos.domain.post.dto.PostDetailsData;
import com.superamigos.domain.post.dto.PostUpdateData;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{userId}")
    @Transactional
    public ResponseEntity create(@RequestBody @Valid PostCreationData data, @PathVariable Long userId, UriComponentsBuilder uriBuilder) {
        Post post = postService.create(data, userId);
        var uri = uriBuilder.path("/posts/{id}").buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(uri).body(new PostDetailsData(post));
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid PostUpdateData data) {
        PostDetailsData postUpdated = postService.update(data);
        return ResponseEntity.ok(postUpdated);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity findAllByAuthorId(@PathVariable Long id) {
        List<PostDetailsData> posts = postService.findAllByAuthorId(id);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity findAllByCreationDate(@PathVariable LocalDate date) {
        List<PostDetailsData> posts = postService.findByCreatedAtDate(date);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteById(@PathVariable Long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
