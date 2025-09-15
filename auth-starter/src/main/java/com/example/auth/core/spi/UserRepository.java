package com.example.auth.core.spi;

import com.example.auth.core.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}
