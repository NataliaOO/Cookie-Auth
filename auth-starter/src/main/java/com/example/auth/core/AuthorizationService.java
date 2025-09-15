package com.example.auth.core;

import java.util.*;

public interface AuthorizationService {
    Optional<List<String>> permissionsFor(String username);
}
