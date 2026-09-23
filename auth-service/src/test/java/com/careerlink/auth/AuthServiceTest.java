package com.careerlink.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.careerlink.auth.dto.LoginRequest;
import com.careerlink.auth.dto.LoginResponse;
import com.careerlink.auth.dto.RegisterResponse;
import com.careerlink.auth.entity.User;
import com.careerlink.auth.jwt.JwtService;
import com.careerlink.auth.repository.UserRepository;
import com.careerlink.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");
        user.setRole("CANDIDATE");
    }

    @Test
    void registerUser() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        RegisterResponse response = authService.register(user);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Test User", response.getName());
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("CANDIDATE", response.getRole());

        verify(userRepository).findByEmail("test@gmail.com");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user);
    }

    @Test
    void loginUser() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "123456"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("test-jwt-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(2L, response.getUserId());
        assertEquals("test-jwt-token", response.getToken());
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("CANDIDATE", response.getRole());

        verify(userRepository).findByEmail("test@gmail.com");
        verify(passwordEncoder).matches("123456", "123456");
        verify(jwtService).generateToken(user);
    }
}