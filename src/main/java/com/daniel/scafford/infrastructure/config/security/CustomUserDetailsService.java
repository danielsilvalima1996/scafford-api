package com.daniel.scafford.infrastructure.config.security;

import com.daniel.scafford.application.ports.out.UserRepositoryPort;
import com.daniel.scafford.domain.model.Permission;
import com.daniel.scafford.domain.model.Role;
import com.daniel.scafford.domain.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    public CustomUserDetailsService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        if (!user.isEnabled()) {
            throw new DisabledException("Este usuário está desativado.");
        }

        // todo: lista de permissoes está vazia
        // Mapeando as permissões dinâmicas do banco para o Spring Security
        List<SimpleGrantedAuthority> authorities = user.roles().stream()
                .filter(Role::isEnabled) // Valida se a Role está ativa
                .flatMap(role -> role.permissions().stream())
                .filter(Permission::isEnabled) // Valida se a Permissão está ativa
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .distinct()
                .toList();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.email())
                .password(user.password())
                .authorities(authorities)
                .disabled(false)
                .build();
    }
}