package com.superamigos.domain.post;

import com.superamigos.domain.post.dto.PostDetailsData;
import com.superamigos.domain.post.dto.PostUpdateData;
import com.superamigos.domain.user.UserRepository;
import com.superamigos.infra.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Post with id " + id + " not found."));
    }

    public PostDetailsData update(PostUpdateData data) {
        Post post = findById(data.id());
        post.setContent(data.content());
        postRepository.save(post);
        return new PostDetailsData(post);
    }

    public List<PostDetailsData> findAllByAuthorId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("Author not found");
        }
        return postRepository.findByAuthorId(id).stream()
            .map(PostDetailsData::new)
            .collect(Collectors.toList());
    }

    public List<PostDetailsData> findByCreatedAtDate(LocalDate date) {
        List<Post> posts = postRepository.findByCreatedAtDate(date);
        return posts.stream()
            .map(PostDetailsData::new)
            .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if(!postRepository.existsById(id)) {
            throw new ValidationException("no post found with the id: " + id);
        }
        postRepository.deleteById(id);
    }
}
