package com.divya.reservation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.divya.reservation.entity.User;
import com.divya.reservation.service.JwtService;
import com.divya.reservation.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody User user) {

        // Public registration can only create USER
        user.setRole("USER");

        return ResponseEntity.ok(
                userService.register(user)
        );
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {
            User user =
                    userService.findByUsername(
                            request.getUsername()
                    );

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword())) {

                return ResponseEntity
                        .status(401)
                        .body("Invalid username or password");
            }

            String token =
                    jwtService.generateToken(user);

            Map<String, Object> response =
                    new HashMap<>();

            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid username or password");
        }
    }

    public static class LoginRequest {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}