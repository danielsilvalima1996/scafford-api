package com.daniel.scafford.infrastructure.adapters.out.persistence;

import com.daniel.scafford.application.ports.out.UserRepositoryPort;
import com.daniel.scafford.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springRepository;

    public UserRepositoryAdapter(SpringDataUserRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Optional<User> findByUsername(String email) {
        return springRepository.findByUsername(email)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getPassword(),
                        entity.isEnabled(),
                        Collections.emptySet() // Lógica de mapeamento de roles aqui
                        // todo: parei aqui, preciso fazer a lista de permissoes
                ));
    }

    @Override
    public User save(User user) {
        // Lógica para converter User (Domínio) -> UserEntity (Infra)
        // e salvar usando o springRepository
        return user;
    }
}