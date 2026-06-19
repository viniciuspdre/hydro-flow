package br.com.project.hydroflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.project.hydroflow.domain.Permission;
import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.repository.PermissionRepository;
import br.com.project.hydroflow.repository.RoleRepository;
import br.com.project.hydroflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleService Tests")
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    @DisplayName("findAllRoles deve retornar lista de RoleDTO")
    void findAllRoles() {
        Role role = new Role("ADMIN");
        when(roleRepository.findAll()).thenReturn(List.of(role));

        List<RoleDTO> result = roleService.findAllRoles();

        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).name());
    }

    @Test
    @DisplayName("findById deve retornar Role se existir")
    void findByIdSuccess() {
        Role role = new Role("ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.findById(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
    }

    @Test
    @DisplayName("findById deve lancar EntityNotFoundException se nao existir")
    void findByIdNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findById(1L));
    }

    @Test
    @DisplayName("saveRole deve salvar e retornar RoleDTO")
    void saveRole() {
        PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
        RoleDTO roleDTO = new RoleDTO(null, "ADMIN", List.of(permDTO));

        Permission perm = new Permission();
        ReflectionTestUtils.setField(perm, "name", "READ");
        ReflectionTestUtils.setField(perm, "label", "Read");
        when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of(perm));

        Role savedRole = new Role("ADMIN");
        savedRole.getPermissions().add(perm);
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        RoleDTO result = roleService.saveRole(roleDTO);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
    }

    @Test
    @DisplayName("saveRole deve lancar excecao se permissao nao existir")
    void saveRolePermissionNotFound() {
        PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
        RoleDTO roleDTO = new RoleDTO(null, "ADMIN", List.of(permDTO));

        when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> roleService.saveRole(roleDTO));
    }

    @Test
    @DisplayName("updateRole deve atualizar e retornar RoleDTO")
    void updateRole() {
        Role role = new Role("USER");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
        RoleDTO roleDTO = new RoleDTO(1L, "ADMIN", List.of(permDTO));

        Permission perm = new Permission();
        ReflectionTestUtils.setField(perm, "name", "READ");
        ReflectionTestUtils.setField(perm, "label", "Read");
        when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of(perm));

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleDTO result = roleService.updateRole(1L, roleDTO);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
    }

    @Test
    @DisplayName("deleteRole deve deletar cargo sem usuarios vinculados")
    void deleteRole() {
        Role role = new Role("ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.existsByRole_Id(1L)).thenReturn(false);

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    @DisplayName("deleteRole deve lancar excecao se houver usuarios vinculados")
    void deleteRoleWithUsers() {
        Role role = new Role("ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.existsByRole_Id(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> roleService.deleteRole(1L));
        verify(roleRepository, never()).delete(any());
    }
}
