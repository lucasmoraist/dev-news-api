package com.devnews.api.repository;

import com.devnews.api.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT p
            FROM t_post p
            WHERE LOWER(p.title)
            LIKE LOWER(CONCAT('%', :title, '%'))
            """)
    List<Post> findByTitle(String title);
}
