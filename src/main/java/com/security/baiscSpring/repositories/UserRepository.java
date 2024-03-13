package com.security.baiscSpring.repositories;

import com.security.baiscSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    public Optional<User> findByUsername(String username);
}
