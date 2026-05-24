package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Permission;

public record PermissionDTO(Long id, String name, String label) {

    public static PermissionDTO from(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getName(), permission.getLabel());
    }
}
