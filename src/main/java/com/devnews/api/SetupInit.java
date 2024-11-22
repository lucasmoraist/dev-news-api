package com.devnews.api;

import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.PostRepository;
import com.devnews.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SetupInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void run(String... args) throws Exception {
        if (activeProfile.equals("open,dev")) {
            List<User> users = this.userRepository.findAll();
            User user = null;
            if (users.isEmpty()) {
                user = new User(
                        null,
                        "John Doe",
                        "john@doe.com",
                        "123456"
                );
                this.userRepository.save(user);
            }

            List<Post> posts = this.postRepository.findAll();
            if (posts.isEmpty()) {
                PostRequest request1 = new PostRequest(
                        "long established",
                        """
                                It is a long established fact that a reader will be distracted by the readable content of
                                a page when looking at its layout. The point of using Lorem Ipsum is that
                                """
                );
                PostRequest request2 = new PostRequest(
                        "What is Lorem Ipsum?",
                        """
                                It is a long established fact that a reader will be distracted by the readable content of
                                a page when looking at its layout. The point of using Lorem Ipsum is that
                                """
                );

                Post post1 = new Post(request1);
                post1.setAuthor(user);
                post1.setImageBanner("1.jpg");

                Post post2 = new Post(request1);
                post2.setAuthor(user);
                post2.setImageBanner("2.jpg");

                Post post3 = new Post(request1);
                post3.setAuthor(user);
                post3.setImageBanner("3.jpg");

                Post post4 = new Post(request1);
                post4.setAuthor(user);
                post4.setImageBanner("4.jpg");

                Post post5 = new Post(request2);
                post5.setAuthor(user);
                post5.setImageBanner("5.jpg");

                this.postRepository.save(post1);
                this.postRepository.save(post2);
                this.postRepository.save(post3);
                this.postRepository.save(post4);
                this.postRepository.save(post5);
            }
        }
    }
}