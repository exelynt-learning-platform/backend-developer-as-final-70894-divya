package com.divya.reservation.service;

import com.divya.reservation.entity.User;
import com.divya.reservation.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {

        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        return repository.save(user);
    }

    public User findByUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid username or password"
                        )
                );
    }
}