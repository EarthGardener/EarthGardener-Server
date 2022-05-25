package com.stopclimatechange.earthgarden.repository;

import com.stopclimatechange.earthgarden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String niakname);
    boolean existsBySocialId(String socialId);

    Optional<User> findByEmail(String email);
    Optional<User> findByPw(String social_id);
}
