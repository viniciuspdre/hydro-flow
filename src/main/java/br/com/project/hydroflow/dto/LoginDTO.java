package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String email,

        @NotBlank @Schema(description = "Senha do usuário (mínimo 8 caracteres)", example = "senha123")
        String password) {}
