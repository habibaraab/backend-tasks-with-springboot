package com.spring.Task6.Controller;
import com.spring.Task6.Repositories.PostRepository;
import com.spring.Task6.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    // Create a new post
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Post> createPost( @RequestBody Post post) {
        post.setStatus("pending"); // Default status
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    // Retrieve all approved posts (public)
    @GetMapping
    public List<Post> getApprovedPosts() {
        return postRepository.findByStatus("approved");
    }

    // Retrieve all posts (Admin, Reviewer)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER')")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Retrieve a specific approved post by ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null || !"approved".equals(post.getStatus())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    // Update a specific post by ID
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,  @RequestBody Post updatedPost) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postRepository.save(post);
        return ResponseEntity.ok(post);
    }

    // Delete a specific post by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Approve a post (Reviewer role)
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('REVIEWER')")
    public ResponseEntity<?> approvePost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.setStatus("approved");
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }
}