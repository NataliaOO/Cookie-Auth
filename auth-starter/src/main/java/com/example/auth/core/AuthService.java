package com.example.auth.core;

import java.util.List;

public interface AuthService {
    boolean authenticate(String username, String rawPassword);
    List<String> permissionsOf(String username);
}
