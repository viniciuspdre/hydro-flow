package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Permission;
import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.UpdateUserDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.Permissions;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("findAllUsers")
    class FindAllUsers {

        @Test
        @DisplayName("deve retornar todos os usuários como DTO sem senha")
        void deveRetornarTodosOsUsuariosComoDtoSemSenha() {
            Role role = roleWithId(1L, "ADMIN");
            User user1 = userWithId(1L, "Maria", "maria@example.com", "hash1", role);
            User user2 = userWithId(2L, "João", "joao@example.com", "hash2", role);
            when(userRepository.findAll()).thenReturn(List.of(user1, user2));

            List<UserDTO> result = userService.findAllUsers();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(1L);
            assertThat(result.get(0).name()).isEqualTo("Maria");
            assertThat(result.get(0).password()).isNull();
            assertThat(result.get(1).id()).isEqualTo(2L);
            assertThat(result.get(1).email()).isEqualTo("joao@example.com");
            verify(userRepository).findAll();
        }
    }

    @Nested
    @DisplayName("saveUser")
    class SaveUser {

        @Test
        @DisplayName("deve buscar cargo, codificar senha, persistir e retornar DTO sem senha")
        void deveBuscarCargoCodificarSenhaPersistirERetornarDtoSemSenha() {
            Role role = roleWithId(1L, "USER");
            when(roleService.findById(1L)).thenReturn(role);

            UserDTO input = new UserDTO(null, "Maria", "maria@example.com", "senha1234", 1L);
            when(passwordEncoder.encode("senha1234")).thenReturn("hash-senha");

            User persisted = userWithId(10L, "Maria", "maria@example.com", "hash-senha", role);
            when(userRepository.save(any(User.class))).thenReturn(persisted);

            UserDTO result = userService.saveUser(input);

            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.name()).isEqualTo("Maria");
            assertThat(result.email()).isEqualTo("maria@example.com");
            assertThat(result.password()).isNull();
            assertThat(result.roleId()).isEqualTo(1L);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User saved = userCaptor.getValue();
            assertThat(saved.getName()).isEqualTo("Maria");
            assertThat(saved.getEmail()).isEqualTo("maria@example.com");
            assertThat(saved.getPassword()).isEqualTo("hash-senha");
            assertThat(saved.getRole()).isSameAs(role);

            verify(roleService).findById(1L);
            verify(passwordEncoder).encode("senha1234");
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("deve atualizar nome e email e reencodar senha quando informada")
        void deveAtualizarNomeEmailEReencodarSenhaQuandoInformada() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(1L, "Antigo", "antigo@example.com", "hash-antigo", role);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(passwordEncoder.encode("novaSenha12")).thenReturn("hash-nova");
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("Novo Nome", "novo@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.REGISTER_DELIVERY));

            UserDTO result = userService.updateUser(1L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(result.email()).isEqualTo("novo@example.com");
            assertThat(existing.getPassword()).isEqualTo("hash-nova");
            assertThat(existing.getRole()).isSameAs(role);
            verify(passwordEncoder).encode("novaSenha12");
            verify(userRepository).save(existing);
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("não deve alterar senha quando password for null")
        void naoDeveAlterarSenhaQuandoPasswordForNull() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(2L, "User", "u@example.com", "hash-original", role);
            when(userRepository.findById(2L)).thenReturn(Optional.of(existing));
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("User X", "ux@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.REGISTER_DELIVERY));

            userService.updateUser(2L, input);

            assertThat(existing.getPassword()).isEqualTo("hash-original");
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("não deve alterar senha quando password for em branco")
        void naoDeveAlterarSenhaQuandoPasswordForEmBranco() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(3L, "User", "u@example.com", "hash-original", role);
            when(userRepository.findById(3L)).thenReturn(Optional.of(existing));
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("User X", "ux@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.EDIT_FAMILY));

            userService.updateUser(3L, input);

            assertThat(existing.getPassword()).isEqualTo("hash-original");
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("deve atualizar cargo quando principal tiver permissão ADMIN e roleId informado")
        void deveAtualizarCargoQuandoPrincipalForAdminERoleIdInformado() {
            Role oldRole = roleWithId(2L, "USER");
            Role newRole = roleWithId(5L, "ADMIN");
            User existing = userWithId(4L, "Alvo", "alvo@example.com", "hash", oldRole);
            when(userRepository.findById(4L)).thenReturn(Optional.of(existing));
            when(roleService.findById(5L)).thenReturn(newRole);
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("Alvo", "alvo@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.ADMIN));

            userService.updateUser(4L, input);

            assertThat(existing.getRole()).isSameAs(newRole);
            verify(roleService).findById(5L);
            verify(passwordEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("deve atualizar cargo quando principal tiver permissão MANAGE_USERS")
        void deveAtualizarCargoQuandoPrincipalTiverManageUsers() {
            Role oldRole = roleWithId(2L, "USER");
            Role newRole = roleWithId(6L, "OPERADOR");
            User existing = userWithId(5L, "Alvo", "alvo2@example.com", "hash", oldRole);
            when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
            when(roleService.findById(6L)).thenReturn(newRole);
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("Alvo", "alvo2@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.MANAGE_USERS));

            userService.updateUser(5L, input);

            assertThat(existing.getRole()).isSameAs(newRole);
            verify(roleService).findById(6L);
        }

        @Test
        @DisplayName("não deve alterar cargo quando principal não for admin mesmo com roleId informado")
        void naoDeveAlterarCargoQuandoPrincipalNaoForAdminMesmoComRoleId() {
            Role oldRole = roleWithId(2L, "USER");
            User existing = userWithId(6L, "Alvo", "alvo3@example.com", "hash", oldRole);
            when(userRepository.findById(6L)).thenReturn(Optional.of(existing));
            when(userRepository.save(existing)).thenReturn(existing);

            UpdateUserDTO input = new UpdateUserDTO("Alvo", "alvo3@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.EDIT_FAMILY));

            userService.updateUser(6L, input);

            assertThat(existing.getRole()).isSameAs(oldRole);
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void deveLancarEntityNotFoundExceptionQuandoIdNaoExistir() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            UpdateUserDTO input = new UpdateUserDTO("X", "x@example.com");
            Authentication auth = authenticationWithPrincipal(principalComPermissoes(Permissions.ADMIN));

            assertThatThrownBy(() -> userService.updateUser(99L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");

            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }
    }

    private static Authentication authenticationWithPrincipal(User principal) {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(principal);
        return auth;
    }

    /**
     * Usuário autenticado com as permissões indicadas (nomes como gravados em {@link Permission}).
     */
    private static User principalComPermissoes(String... permissionNames) {
        Role role = new Role("CALLER");
        role.setId(999L);
        for (String name : permissionNames) {
            role.getPermissions().add(permissionWithName(name));
        }
        return userWithId(900L, "Caller", "caller@test.com", "hash", role);
    }

    private static Permission permissionWithName(String name) {
        Permission p = new Permission();
        setField(p, "name", name);
        return p;
    }

    private static Role roleWithId(Long id, String name) {
        Role role = new Role(name);
        role.setId(id);
        return role;
    }

    private static User userWithId(Long id, String name, String email, String password, Role role) {
        User user = new User(name, email, password, role);
        setField(user, "id", id);
        return user;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
