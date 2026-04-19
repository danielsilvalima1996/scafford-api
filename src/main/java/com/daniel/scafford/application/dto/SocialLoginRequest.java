package com.daniel.scafford.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(
        @NotBlank(message = "O token do Google é obrigatório")
        String token
) {}