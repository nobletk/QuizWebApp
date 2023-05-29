package com.example.QuizWebApp;

import com.example.QuizWebApp.Model.UserAuthentication.User;
import com.example.QuizWebApp.Model.UserAuthentication.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthenticationControllerTest.TestConfig.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithAnonymousUser
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_success() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(null);

        mockMvc.perform(post("/signup")
                        .param("username", "testuser")
                        .param("password", "testpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(userService, times(1)).registerUser("testuser", "testpassword");
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_usernameTaken() throws Exception {
        User existingUser = new User("testuser", "testpassword");
        when(userService.findByUsername("testuser")).thenReturn(existingUser);

        mockMvc.perform(post("/signup")
                        .param("username", "testuser")
                        .param("password", "testpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("errorMessage", "Username is already taken!"));

        verify(userService, times(0)).registerUser(anyString(), anyString());
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestConfig {

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests()
                    .anyRequest().permitAll()
                    .and()
                    .csrf().disable();
            return http.build();
        }
    }
}

