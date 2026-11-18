package com.rescuebite.user;

import com.rescuebite.user.dto.RegisterRequest;
import com.rescuebite.user.model.Role;
import com.rescuebite.user.model.User;
import com.rescuebite.user.repository.UserRepository;
import com.rescuebite.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword123");
    }

    @Test
    @DisplayName("Should register customer user successfully and return JWT token")
    void testRegisterUser() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User mockSaved = new User("Alice", "test@example.com", "hashedPassword123", "+94771234567", Role.CUSTOMER, "Colombo");
        mockSaved.setId("usr-12345");

        when(userRepository.save(any(User.class))).thenReturn(mockSaved);

        RegisterRequest req = new RegisterRequest("Alice", "test@example.com", "Password123!", "+94771234567", Role.CUSTOMER, "Colombo");
        var resp = authService.register(req);

        assertNotNull(resp);
        assertEquals("Alice", resp.getName());
        assertEquals("test@example.com", resp.getEmail());
        assertEquals(Role.CUSTOMER, resp.getRole());
        assertNotNull(resp.getToken());
    }
}

