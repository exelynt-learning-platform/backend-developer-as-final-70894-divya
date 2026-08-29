package com.divya.reservation.controller;

import com.divya.reservation.entity.User;
import com.divya.reservation.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    // REGISTER
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    // FIND USER
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return service.findByUsername(username);
    }
}