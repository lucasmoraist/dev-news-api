package com.devnews.api;

import com.devnews.api.domain.dto.post.PostRequest;
import com.devnews.api.domain.entity.Post;
import com.devnews.api.domain.entity.User;
import com.devnews.api.repository.PostRepository;
import com.devnews.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SetupInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private static final String DEFAULT_USER_NAME = "John Doe";
    private static final String DEFAULT_USER_EMAIL = "john@doe.com";
    private static final String DEFAULT_USER_PASSWORD = "123456";
    private static final String POST_TITLE = "What is Lorem Ipsum?";
    private static final String POST_CONTENT = """
            It is a long established fact that a reader will be distracted by the readable content of
            a page when looking at its layout. The point of using Lorem Ipsum is that
            """;

    @Override
    public void run(String... args) throws Exception {
        if (isDevOrOpenProfile()) {
            log.info("Inicializando dados de desenvolvimento...");
            initializeUsers();
            initializePosts();
            log.info("Dados de desenvolvimento inicializados.");
        }
    }

    private boolean isDevOrOpenProfile() {
        return "open,dev".equals(activeProfile);
    }

    private void initializeUsers() {
        if (userRepository.findAll().isEmpty()) {
            User user = User.builder()
                    .name(DEFAULT_USER_NAME)
                    .email(DEFAULT_USER_EMAIL)
                    .password(passwordEncoder.encode(DEFAULT_USER_PASSWORD))
                    .build();
            userRepository.save(user);
            log.info("Usuário criado: {}", user);
        }
    }

    private void initializePosts() {
        if (postRepository.findAll().isEmpty()) {
            User user = userRepository.findByEmail(DEFAULT_USER_EMAIL).orElseThrow(
                    () -> new IllegalStateException("Usuário padrão não encontrado.")
            );

            for (int i = 0; i < 4; i++) {
                savePost(user);
            }

            PostRequest customPostRequest = new PostRequest(POST_TITLE, POST_CONTENT);
            savePost(user, customPostRequest);
        }
    }

    private void savePost(User user) {
        Post post = getPost(user);
        postRepository.save(post);
        log.info("Post criado: {}", post);
    }

    private void savePost(User user, PostRequest request) {
        Post post = new Post(request);
        post.setAuthor(user);
        postRepository.save(post);
        log.info("Post personalizado criado: {}", post);
    }

    private Post getPost(User user) {
        Post post = new Post(new PostRequest(POST_TITLE, POST_CONTENT));
        post.setAuthor(user);
        return post;
    }

}
