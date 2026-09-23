package com.careerlink.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
class AuthServiceApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegister() {

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(userRepository.save(user))
                .thenReturn(user);

        RegisterResponse result =
                authService.register(user);

        assertNotNull(result);

        verify(userRepository, times(1))
                .findByEmail("test@gmail.com");

        verify(passwordEncoder, times(1))
                .encode("123456");

        verify(userRepository, times(1))
                .save(user);
    }

    @Test
    void testLogin() {

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("test-jwt-token");

        LoginResponse result =
                authService.login(request);

        assertNotNull(result);
        assertEquals(
                "test-jwt-token",
                result.getToken());

        verify(userRepository, times(1))
                .findByEmail("test@gmail.com");

        verify(passwordEncoder, times(1))
                .matches("123456", "encodedPassword");

        verify(jwtService, times(1))
                .generateToken(user);
    }
}