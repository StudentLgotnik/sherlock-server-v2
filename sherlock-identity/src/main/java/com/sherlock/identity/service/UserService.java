package com.sherlock.identity.service;

import com.sherlock.identity.persistance.entity.User;
import com.sherlock.identity.persistance.repository.UserRepository;
import com.sherlock.identity.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(User user) {
        Objects.requireNonNull(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(User user) {
        Objects.requireNonNull(user.getPassword());
        return userRepository.findByEmail(user.getEmail())
                .filter(u -> passwordEncoder.matches(user.getPassword(), u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User getOrCreate(User user) {
        return userRepository.findByEmail(user.getEmail())
                .orElseGet(() -> {
                    user.setPassword(UUID.randomUUID().toString());
                    return signUp(user);
                });
    }
}
