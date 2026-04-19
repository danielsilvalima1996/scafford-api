package com.daniel.scafford.domain.model;

import java.util.Set;

public record User(
        Long id,
        String email,
        String password,
        boolean isEnabled,
        Set<Role> roles
) {}