package com.demo.store.repositories;

import com.demo.store.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email);

    Optional<User> findByEmail(String email);
}
