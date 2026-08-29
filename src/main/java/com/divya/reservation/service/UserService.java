package com.divya.reservation.service;

import com.divya.reservation.entity.User;
import com.divya.reservation.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER USER
    public User register(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return repository.save(user);
    }

    // FIND USER BY USERNAME
    public User findByUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
    }
}