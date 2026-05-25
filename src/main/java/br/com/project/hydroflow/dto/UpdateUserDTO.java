package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank @Schema(description = "Nome do Usuário", example = "joao teste")
        String name,

        @NotBlank @Email String email) {}
