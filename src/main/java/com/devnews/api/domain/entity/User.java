package com.devnews.api.domain.entity;

import com.devnews.api.domain.dto.user.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    public User(UserRequest request) {
        this.name = request.name();
        this.email = request.email();
        this.password = request.password();
    }

}
