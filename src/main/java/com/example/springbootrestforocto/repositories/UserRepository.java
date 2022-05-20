package com.example.springbootrestforocto.repositories;
import com.example.springbootrestforocto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
Optional <User> findAllByAuthorities();

    Optional<User> findAllByAuthorities(String username, String password);
}
