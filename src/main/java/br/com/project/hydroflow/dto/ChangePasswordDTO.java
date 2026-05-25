package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record ChangePasswordDTO(
        Long userId,

        @Nullable @Schema(description = "Senha atual do usuário", example = "senhaAtual123", nullable = true)
        String currentPassword,

        @Schema(description = "Nova senha do usuário", example = "novaSenha456", nullable = true)
        String newPassword) {}
