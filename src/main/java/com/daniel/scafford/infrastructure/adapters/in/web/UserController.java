package com.daniel.scafford.infrastructure.adapters.in.web;

import com.daniel.scafford.application.dto.UserRequest;
import com.daniel.scafford.application.ports.in.CreateUserUseCase;
import com.daniel.scafford.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<Void> create(@Valid @RequestBody UserRequest request) {
        // Conversão simples de DTO para Domínio
        User domainUser = new User(
                null,
                request.email(),
                request.password(),
                true,
                null
        );

        createUserUseCase.execute(domainUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Lógica...
        return ResponseEntity.noContent().build();
    }
}