package com.daniel.scafford.infrastructure.adapters.in.web;

import com.daniel.scafford.application.dto.LoginRequest;
import com.daniel.scafford.application.dto.SocialLoginRequest;
import com.daniel.scafford.application.dto.TokenResponse;
import com.daniel.scafford.application.ports.out.UserRepositoryPort;
import com.daniel.scafford.domain.model.User;
import com.daniel.scafford.infrastructure.config.security.GoogleAuthService;
import com.daniel.scafford.infrastructure.config.security.JwtProvider;
import com.daniel.scafford.infrastructure.config.security.UserDetailsMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserRepositoryPort userRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager; // Adicionado para auth tradicional

    public AuthController(GoogleAuthService googleAuthService,
                          UserRepositoryPort userRepository,
                          JwtProvider jwtProvider,
                          AuthenticationManager authenticationManager) {
        this.googleAuthService = googleAuthService;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        // 1. Autentica as credenciais (Email e Senha)
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        // 2. Recupera o UserDetails (CustomUserDetailsService que corrigimos antes)
        var userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o Token com a expiração de 2 horas configurada
        String token = jwtProvider.generateToken(userDetails);

        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/google")
    public ResponseEntity<String> loginWithGoogle(@RequestBody SocialLoginRequest request) {
        var payload = googleAuthService.verify(request.token());

        var user = userRepository.findByUsername(payload.getEmail())
                .filter(User::isEnabled)
                .orElseThrow(() -> new BadCredentialsException("Usuário não cadastrado ou inativo"));

        return ResponseEntity.ok(jwtProvider.generateToken(UserDetailsMapper.build(user)));
    }
}