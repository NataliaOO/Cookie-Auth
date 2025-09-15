package com.example.auth.core;

import com.example.auth.core.model.User;
import com.example.auth.core.spi.UserRepository;
import com.example.auth.crypto.PasswordHasher;

import java.util.*;

public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordHasher hasher;

    public AuthServiceImpl(UserRepository users, PasswordHasher hasher) {
        this.users = users;
        this.hasher = hasher;
    }

    @Override
    public boolean authenticate(String username, String rawPassword) {
        Optional<User> u = users.findByUsername(username);
        return u.map(user -> hasher.matches(rawPassword, user.passwordHash()))
                .orElse(false);
    }

    @Override
    public List<String> permissionsOf(String username) {
        return users.findByUsername(username)
                .map(User::permissions)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }
}
