package com.example.auth.core.model;

import java.util.*;

public class User {
    private final String username;
    private final String passwordHash;
    private final Set<String> roles = new LinkedHashSet<>();
    private final List<String> permissions = new ArrayList<>();

    public User(String username, String passwordHash) {
        this.username = Objects.requireNonNull(username);
        this.passwordHash = Objects.requireNonNull(passwordHash);
    }
    public String username() { return username; }
    public String passwordHash() { return passwordHash; }
    public Set<String> roles() { return roles; }
    public List<String> permissions() { return permissions; }
}