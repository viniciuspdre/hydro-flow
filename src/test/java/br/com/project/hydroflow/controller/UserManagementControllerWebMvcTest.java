package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.PermissionService;
import br.com.project.hydroflow.service.RoleService;
import br.com.project.hydroflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Contrato HTTP — UserManagementController")
class UserManagementControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private PermissionService permissionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("GET /hf/user-management lista todos os usuários")
    void findAllUsers() throws Exception {
        UserDTO user = new UserDTO(1L, "Admin", "admin@teste.com", "senha123", 1L);
        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/hf/user-management"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Admin"));
    }

    @Test
    @DisplayName("GET /hf/user-management/roles lista todos os cargos")
    void findAllRoles() throws Exception {
        RoleDTO role = new RoleDTO(1L, "ADMIN", List.of());
        when(roleService.findAllRoles()).thenReturn(List.of(role));

        mockMvc.perform(get("/hf/user-management/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }

    @Test
    @DisplayName("GET /hf/user-management/permissions lista todas as permissões")
    void findAllPermissions() throws Exception {
        PermissionDTO perm = new PermissionDTO(1L, "READ_ALL", "Ler tudo");
        when(permissionService.findAllPermissions()).thenReturn(List.of(perm));

        mockMvc.perform(get("/hf/user-management/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("READ_ALL"));
    }

    @Test
    @DisplayName("POST /hf/user-management/create-role cria cargo")
    void createRole() throws Exception {
        RoleDTO request = new RoleDTO(null, "ADMIN", List.of());
        RoleDTO response = new RoleDTO(1L, "ADMIN", List.of());

        when(roleService.saveRole(any(RoleDTO.class))).thenReturn(response);

        mockMvc.perform(post("/hf/user-management/create-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /hf/user-management/roles/{id} atualiza cargo")
    void updateRole() throws Exception {
        RoleDTO request = new RoleDTO(1L, "ADMIN", List.of());
        when(roleService.updateRole(eq(1L), any(RoleDTO.class))).thenReturn(request);

        mockMvc.perform(put("/hf/user-management/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @DisplayName("DELETE /hf/user-management/roles/{id} deleta cargo")
    void deleteRole() throws Exception {
        doNothing().when(roleService).deleteRole(1L);

        mockMvc.perform(delete("/hf/user-management/roles/1"))
                .andExpect(status().isNoContent());
    }
}
