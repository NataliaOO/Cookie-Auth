package com.example.auth.core;

import com.example.auth.core.model.User;
import com.example.auth.core.spi.UserRepository;

import java.util.*;

public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserRepository users;

    public AuthorizationServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public Optional<List<String>> permissionsFor(String username) {
        return users.findByUsername(username).map(User::permissions);
    }
}
