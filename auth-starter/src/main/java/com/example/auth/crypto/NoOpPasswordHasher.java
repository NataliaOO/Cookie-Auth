package com.example.auth.crypto;

import java.util.Objects;

public final class NoOpPasswordHasher implements PasswordHasher {
    @Override public String hash(String raw) { return raw == null ? "" : raw; }
    @Override public boolean matches(String raw, String hashed) {
        return Objects.equals(hash(raw), hashed);
    }
}