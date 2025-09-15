package com.example.auth.starter.autoconfigure;

import com.example.auth.adapters.mem.InMemoryUserRepository;
import com.example.auth.core.AuthService;
import com.example.auth.core.AuthServiceImpl;
import com.example.auth.core.AuthorizationService;
import com.example.auth.core.AuthorizationServiceImpl;
import com.example.auth.core.spi.UserRepository;
import com.example.auth.crypto.NoOpPasswordHasher;
import com.example.auth.crypto.PasswordHasher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PasswordHasher passwordHasher() {
        return new NoOpPasswordHasher();
    }

    @Bean
    @ConditionalOnMissingBean(UserRepository.class)
    public UserRepository userRepository(PasswordHasher hasher) {
        return new InMemoryUserRepository(hasher);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService(UserRepository users, PasswordHasher hasher) {
        return new AuthServiceImpl(users, hasher);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationService authorizationService(UserRepository users) {
        return new AuthorizationServiceImpl(users);
    }
}