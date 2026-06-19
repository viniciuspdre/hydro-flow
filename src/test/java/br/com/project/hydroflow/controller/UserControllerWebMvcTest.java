package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.dto.UpdateUserDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes para UserController")
class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("deve criar novo usuário")
        void testCreateNewUser() throws Exception {
            UserDTO request = new UserDTO(null, "Novo Usuario", "novo@teste.com", "senha123", 1L);
            UserDTO response = new UserDTO(1L, "Novo Usuario", "novo@teste.com", "senha123", 1L);

            when(userService.saveUser(any(UserDTO.class))).thenReturn(response);

            mockMvc.perform(post("/hf/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("deve atualizar usuário")
        void testUpdateUser() throws Exception {
            UpdateUserDTO request = new UpdateUserDTO("Atualizado", "atualizado@teste.com");
            UserDTO response = new UserDTO(1L, "Atualizado", "atualizado@teste.com", "senha123", 1L);

            when(userService.updateUser(eq(1L), any(UpdateUserDTO.class))).thenReturn(response);

            mockMvc.perform(put("/hf/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Atualizado"));
        }
    }
}
