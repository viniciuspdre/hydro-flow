package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record RoleDTO(
        Long id,

        @NotBlank @Schema(description = "Nome do cargo", example = "OPERADOR")
        String name,

        List<PermissionDTO> permissions) {

    public static RoleDTO from(Role role) {
        List<PermissionDTO> permissions =
                role.getPermissions().stream().map(PermissionDTO::from).toList();

        return new RoleDTO(role.getId(), role.getName(), permissions);
    }
}
