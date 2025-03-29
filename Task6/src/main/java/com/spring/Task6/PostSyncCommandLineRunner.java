package com.spring.Task6;

import com.spring.Task6.Repositories.PostRepository;
import com.spring.Task6.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class PostSyncCommandLineRunner implements CommandLineRunner {
    @Autowired
    private PostRepository postRepository;

    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {
        String apiUrl = "https://jsonplaceholder.typicode.com/posts";
        Post[] posts = restTemplate.getForObject(apiUrl, Post[].class);
        if (posts != null) {
            for (Post post : posts) {
                if (postRepository.findByTitle(post.getTitle()) == null) {
                    post.setStatus("pending");
                    postRepository.save(post);
                }
            }
            System.out.println("Posts synced successfully.");
        }
    }
}