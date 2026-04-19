package com.daniel.scafford.infrastructure.config.security;

import com.daniel.scafford.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsMapper {
    public static UserDetails build(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.email())
                .password(user.password())
                .disabled(!user.isEnabled())
                .authorities(user.roles().stream()
                        .flatMap(role -> role.permissions().stream())
                        .map(p -> new SimpleGrantedAuthority(p.name()))
                        .toList())
                .build();
    }
}