package com.security.baiscSpring.repositories;

import com.security.baiscSpring.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {


    Optional<RefreshToken> findByRefreshToken(String token);
}
