package com.devnews.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "t_comment")
@Entity(name = "t_comment")
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
