package com.superamigos.controller;

import com.superamigos.domain.post.Post;
import com.superamigos.domain.post.PostRepository;
import com.superamigos.domain.post.PostService;
import com.superamigos.domain.post.dto.PostCreationData;
import com.superamigos.domain.post.dto.PostDetailsData;
import com.superamigos.domain.post.dto.PostUpdateData;
import com.superamigos.domain.user.User;
import com.superamigos.domain.user.UserService;
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

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostService postService;

    public PostController(PostRepository postRepository, UserService userService, PostService postService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/{userId}")
    @Transactional
    public ResponseEntity create(@RequestBody @Valid PostCreationData data, @PathVariable Long userId, UriComponentsBuilder uriBuilder) {
        User user = userService.findById(userId);
        Post post = new Post(data, user);
        postRepository.save(post);
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
    public ResponseEntity deleteById(Long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
