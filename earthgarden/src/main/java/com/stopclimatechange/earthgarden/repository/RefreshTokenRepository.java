package com.stopclimatechange.earthgarden.repository;

import com.stopclimatechange.earthgarden.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUserId(String userId);
    Boolean existsByUserId(String userID);
}
