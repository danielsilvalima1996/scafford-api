package com.daniel.scafford.application.ports.in;

import com.daniel.scafford.domain.model.User;

public interface CreateUserUseCase {
    User execute(User user);
}