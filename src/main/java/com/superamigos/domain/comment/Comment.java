package com.superamigos.domain.comment;

import com.superamigos.domain.comment.dto.CommentCreationData;
import com.superamigos.domain.post.Post;
import com.superamigos.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private User author;
    @ManyToOne
    private Post post;

    public Comment(CommentCreationData data, User user, Post post) {
        this.content = data.content();
        this.author = user;
        this.post = post;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
