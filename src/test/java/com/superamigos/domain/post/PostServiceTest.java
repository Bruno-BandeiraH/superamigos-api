package com.superamigos.domain.post;

import com.superamigos.domain.post.dto.PostCreationData;
import com.superamigos.domain.post.dto.PostDetailsData;
import com.superamigos.domain.post.dto.PostUpdateData;
import com.superamigos.domain.user.User;
import com.superamigos.domain.user.UserRepository;
import com.superamigos.domain.user.UserService;
import com.superamigos.infra.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private PostService postService;


    private User author;
    private Post existingPost;
    private PostCreationData postCreationData;

    @BeforeEach
    void setUp() {
        // Setup author
        author = new User();
        author.setId(1L);
        author.setUsername("bruno.bandeira");
        author.setName("Bruno Bandeira");

        // Setup existhing post
        existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setContent("conteudo do post");
        existingPost.setComments(new ArrayList<>());
        existingPost.setAuthor(author);
        existingPost.setCreatedAt(LocalDateTime.now().minusDays(1));

        // Setup creational DTO
        postCreationData = new PostCreationData("Novo conteúdo do post");
    }

    @Test
    @DisplayName("should create post with valid author")
    void shouldCreatePostWithValidAuthor() {
        // given
        Long authorId = 1L;
        when(userService.findById(authorId)).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setId(1L); // Simula o ID gerado
            return savedPost;
        });

        // when
        Post result = postService.create(postCreationData, authorId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("Novo conteúdo do post");
        assertThat(result.getAuthor()).isEqualTo(author);

        verify(userService).findById(authorId);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("should throw exception when creating a post with invalid author")
    void shouldThrowExceptionWhenCreatingPostWithInvalidAuthor() {
        // Given
        Long invalidAuthorId = 999L;
        when(userService.findById(invalidAuthorId))
            .thenThrow(new ValidationException("User with id " + invalidAuthorId + " not found."));

        // When & Then
        assertThatThrownBy(() -> postService.create(postCreationData, invalidAuthorId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("User with id " + invalidAuthorId + " not found");

        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("should return posts by author when he exists")
    void shouldReturnPostsByAuthorWhenAuthorExists() {
        // Given
        Long authorId = 1L;
        List<Post> posts = Arrays.asList(existingPost);

        when(userRepository.existsById(authorId)).thenReturn(true);
        when(postRepository.findByAuthorId(authorId)).thenReturn(posts);

        // When
        List<PostDetailsData> result = postService.findAllByAuthorId(authorId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).content()).isEqualTo("conteudo do post");
        assertThat(result.get(0).authorName()).isEqualTo("Bruno Bandeira");

        verify(userRepository).existsById(authorId);
        verify(postRepository).findByAuthorId(authorId);
    }

    @Test
    @DisplayName("should throw exception when finding posts by non existing author")
    void shouldThrowExceptionWhenFindingPostsByNonExistingAuthor() {
        // Given
        Long invalidAuthorId = 999L;
        when(userRepository.existsById(invalidAuthorId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> postService.findAllByAuthorId(invalidAuthorId))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Author not found");

        verify(postRepository, never()).findByAuthorId(any());
    }

    @Test
    @DisplayName("should return posts filtered by creation date")
    void shouldReturnPostsFilteredByCreationDate() {
        // Given
        LocalDate targetDate = LocalDate.now().minusDays(1);
        List<Post> posts = Arrays.asList(existingPost);

        when(postRepository.findByCreatedAtDate(targetDate)).thenReturn(posts);

        // When
        List<PostDetailsData> result = postService.findByCreatedAtDate(targetDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        verify(postRepository).findByCreatedAtDate(targetDate);
    }

    @Test
    @DisplayName("should return empty list when no posts on date")
    void shouldReturnEmptyListWhenNoPostsOnDate() {
        // Given
        LocalDate targetDate = LocalDate.now().minusYears(1); // Data sem posts
        when(postRepository.findByCreatedAtDate(targetDate)).thenReturn(Arrays.asList());

        // When
        List<PostDetailsData> result = postService.findByCreatedAtDate(targetDate);

        // Then
        assertThat(result).isEmpty();
        verify(postRepository).findByCreatedAtDate(targetDate);
    }

    @Test
    @DisplayName("should delete existing post")
    void shouldDeleteExistingPost() {
        // Given
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);
        doNothing().when(postRepository).deleteById(postId);

        // When
        postService.deleteById(postId);

        // Then
        verify(postRepository).existsById(postId);
        verify(postRepository).deleteById(postId);
    }

    @Test
    @DisplayName("should throw exception when deleting non existing post")
    void shouldThrowExceptionWhenDeletingNonExistingPost() {
        // Given
        Long invalidPostId = 999L;
        when(postRepository.existsById(invalidPostId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> postService.deleteById(invalidPostId))
            .isInstanceOf(ValidationException.class)
            .hasMessage("no post found with the id: " + invalidPostId);

        verify(postRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("should update existing post")
    void shouldUpdateExistingPost() {
        // Given
        PostUpdateData updateData = new PostUpdateData(1L, "Conteúdo atualizado");
        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        // When
        PostDetailsData result = postService.update(updateData);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(existingPost.getContent()).isEqualTo("Conteúdo atualizado");

        verify(postRepository).findById(1L);
        verify(postRepository).save(existingPost);
    }

    @Test
    @DisplayName("should find post by id")
    void shouldFindPostById() {
        // Given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // When
        Post result = postService.findById(postId);

        // Then
        assertThat(result).isEqualTo(existingPost);
        verify(postRepository).findById(postId);
    }

    @Test
    @DisplayName("should throw exception when finding posts by non existing id")
    void shouldThrowExceptionWhenFindingPostByNonExistingId() {
        // Given
        Long invalidPostId = 999L;
        when(postRepository.findById(invalidPostId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.findById(invalidPostId))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Post with id " + invalidPostId + " not found.");
    }

}