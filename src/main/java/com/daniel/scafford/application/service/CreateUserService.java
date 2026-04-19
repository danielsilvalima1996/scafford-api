package com.daniel.scafford.application.service;

import com.daniel.scafford.application.ports.in.CreateUserUseCase;
import com.daniel.scafford.application.ports.out.UserRepositoryPort;
import com.daniel.scafford.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User execute(User user) {
        // Regra de Negócio: Criptografar senha antes de salvar
        // No Java 21 (Records), criamos uma nova instância com a senha alterada
        User userWithEncodedPassword = new User(
                user.id(),
                user.email(),
                passwordEncoder.encode(user.password()),
                true, // isEnabled padrão
                user.roles()
        );

        return userRepositoryPort.save(userWithEncodedPassword);
    }
}