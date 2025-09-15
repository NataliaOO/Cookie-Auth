package com.example.auth.adapters.mem;

import com.example.auth.core.model.User;
import com.example.auth.core.spi.UserRepository;
import com.example.auth.crypto.PasswordHasher;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();
    private final PasswordHasher hasher;

    public InMemoryUserRepository(PasswordHasher hasher) {
        this.hasher = hasher;
        seed();
    }

    private void seed() {
        // user1 / pass1 : KM ; p1,p2,p3
        User u1 = new User("user1", hasher.hash("pass1"));
        u1.roles().add("KM");
        u1.permissions().addAll(List.of("p1","p2","p3"));
        save(u1);

        // user2 / pass2 : KM,GKM ; p1..p5
        User u2 = new User("user2", hasher.hash("pass2"));
        u2.roles().addAll(List.of("KM","GKM"));
        u2.permissions().addAll(List.of("p1","p2","p3","p4","p5"));
        save(u2);
    }

    @Override public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
    @Override public void save(User user) { users.put(user.username(), user); }
}
