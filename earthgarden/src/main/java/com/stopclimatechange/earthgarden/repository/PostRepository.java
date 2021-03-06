package com.stopclimatechange.earthgarden.repository;

import com.stopclimatechange.earthgarden.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Post findById(String id);
}
