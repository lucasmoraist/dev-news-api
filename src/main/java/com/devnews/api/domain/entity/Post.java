package com.devnews.api.domain.entity;

import com.devnews.api.domain.dto.post.PostRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@Entity(name = "post")
@EqualsAndHashCode(of = "id")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(name = "image_banner", nullable = false)
    private String imageBanner;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Post(PostRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.imageBanner = request.imageBanner();
    }

    public void update(PostRequest request) {
        if (request.title() != null) {
            this.title = request.title();
        }
        if (request.content() != null) {
            this.content = request.content();
        }
        if (request.imageBanner() != null) {
            this.imageBanner = request.imageBanner();
        }
    }

}
