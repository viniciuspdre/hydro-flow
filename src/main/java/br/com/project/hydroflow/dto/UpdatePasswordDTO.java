package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotBlank @Schema(description = "Senha atual do usuário", example = "senhaAtual123")
        String currentPassword,

        @NotBlank
        @Size(min = 8, message = "A nova senha deve ter pelo menos 8 caracteres")
        @Schema(description = "Nova senha (mínimo 8 caracteres)", example = "novaSenha123")
        String newPassword) {}
