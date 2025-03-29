package com.spring.Task6.Repositories;

import com.spring.Task6.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatus(String status);
    Post findByTitle(String title); // For sync uniqueness check
}