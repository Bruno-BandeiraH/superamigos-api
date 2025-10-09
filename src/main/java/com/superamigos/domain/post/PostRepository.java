package com.superamigos.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(Long authorId);

    @Query("SELECT p FROM Post p WHERE DATE(p.createdAt) = :date")
    List<Post> findByCreatedAtDate(LocalDate date);
}
