package com.devnews.api.repository;

import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.dto.user.UserRequest;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        UserRequest request = new UserRequest(
                "John Doe",
                "john@doe.com",
                "123456"
        );
        user = new User(request);
        this.userRepository.save(user);
    }

    @Test
    @DisplayName("Deve retornar uma lista de posts com base no título")
    public void case01() {
        // Given: cria um post para o teste
        PostRequest request1 = new PostRequest(
                "Spring Boot Tutorial",
                "This is a tutorial on Spring Boot.",
                "image.jpg"
        );
        Post post1 = new Post(request1);
        post1.setAuthor(user);

        PostRequest request2 = new PostRequest(
                "Java Persistence API",
                "An introduction to JPA and Spring Data JPA.",
                "image.jpg"
        );
        Post post2 = new Post(request2);
        post2.setAuthor(user);

        postRepository.save(post1);
        postRepository.save(post2);

        // When: chama o método findByTitle
        List<Post> posts = postRepository.findByTitle("Spring");

        // Then: verifica se o post com o título "Spring Boot Tutorial" foi encontrado
        assertFalse(posts.isEmpty());
        assertTrue(posts.stream().anyMatch(p -> p.getTitle().equals("Spring Boot Tutorial")));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não encontrar posts com base no título")
    public void case02() {
        // When: chama o método findByTitle com um título que não existe
        List<Post> posts = postRepository.findByTitle("Non Existent Title");

        // Then: o resultado deve ser vazio
        assertTrue(posts.isEmpty());
    }

}