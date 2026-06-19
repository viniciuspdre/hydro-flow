package br.com.project.hydroflow.security;

import static org.junit.jupiter.api.Assertions.*;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(
                jwtService, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L); // 1 hour
    }

    @Test
    @DisplayName("generateToken e extractEmail devem funcionar corretamente")
    void generateTokenAndExtractEmail() {
        User user = new User("Test", "test@example.com", "pass", new Role("USER"));
        ReflectionTestUtils.setField(user, "id", 1L);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals("test@example.com", extractedEmail);
    }

    @Test
    @DisplayName("isTokenValid deve retornar true para token valido")
    void isTokenValid() {
        User user = new User("Test", "test@example.com", "pass", new Role("USER"));
        ReflectionTestUtils.setField(user, "id", 1L);

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("pass")
                .authorities("USER")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("isTokenValid deve retornar false para email incorreto")
    void isTokenInvalidEmail() {
        User user = new User("Test", "test@example.com", "pass", new Role("USER"));
        ReflectionTestUtils.setField(user, "id", 1L);

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("wrong@example.com")
                .password("pass")
                .authorities("USER")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("isTokenValid deve retornar false para token expirado")
    void isTokenExpired() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L); // Expired

        User user = new User("Test", "test@example.com", "pass", new Role("USER"));
        ReflectionTestUtils.setField(user, "id", 1L);

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("pass")
                .authorities("USER")
                .build();

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }
}
