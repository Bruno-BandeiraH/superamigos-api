package com.superamigos.domain.comment;

import com.superamigos.domain.comment.dto.CommentCreationData;
import com.superamigos.domain.comment.dto.CommentDetailsData;
import com.superamigos.domain.post.Post;
import com.superamigos.domain.post.PostService;
import com.superamigos.domain.user.User;
import com.superamigos.domain.user.UserService;
import com.superamigos.infra.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public Comment create(CommentCreationData data, Long userId, Long postId) {
        User user = userService.findById(userId);
        Post post = postService.findById(postId);
        Comment comment = new Comment(data, user, post);
        commentRepository.save(comment);
        return comment;
    }

    public void deleteById(Long id) {
        if(!commentRepository.existsById(id)) {
            throw new ValidationException("Comment with id: " + id + " not found");
        }
        commentRepository.deleteById(id);
    }

    public List<CommentDetailsData> findByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
            .map(CommentDetailsData::new)
            .collect(Collectors.toList());
    }

    public CommentDetailsData findById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Comment with id: " + id + " not found"));
        return new CommentDetailsData(comment);
    }
}
