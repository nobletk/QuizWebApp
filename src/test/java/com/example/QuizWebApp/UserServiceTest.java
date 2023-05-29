package com.example.QuizWebApp;

import com.example.QuizWebApp.Model.UserAuthentication.User;
import com.example.QuizWebApp.Model.UserAuthentication.UserRepository;
import com.example.QuizWebApp.Model.UserAuthentication.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testuser", "hashedpassword");
        user.setId(1L);
    }

    @Test
    public void registerUserTest() {
        when(passwordEncoder.encode("password")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser("testuser", "password");

        assertEquals(user, result);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void findByUsernameTest() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.findByUsername("testuser");

        assertEquals(user, result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void validateUserCredentialsTest() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password", "hashedpassword")).thenReturn(true);

        boolean validCredentials = userService.validateUserCredentials("testuser", "password");

        assertTrue(validCredentials);
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password", "hashedpassword");
    }

    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails result = userService.loadUserByUsername("testuser");

        assertEquals("testuser", result.getUsername());
        assertEquals("hashedpassword", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void loadUserByUsernameNotFoundTest() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
        verify(userRepository).findByUsername("testuser");
    }
}

