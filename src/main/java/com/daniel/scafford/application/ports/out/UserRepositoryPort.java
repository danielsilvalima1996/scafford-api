package com.daniel.scafford.application.ports.out;

import com.daniel.scafford.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    User save(User user);
}