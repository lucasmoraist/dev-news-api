package com.devnews.api.domain.entity;

import com.devnews.api.domain.dto.user.UserRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
@Entity(name = "t_user")
@EqualsAndHashCode(of = "id")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false, length = 180)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    public User(UserRequest request) {
        this.name = request.name();
        this.email = request.email();
        this.password = request.password();
    }

}
