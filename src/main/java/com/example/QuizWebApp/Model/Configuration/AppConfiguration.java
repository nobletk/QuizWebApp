package com.example.QuizWebApp.Model.Configuration;

import com.example.QuizWebApp.Model.Login.UserRepository;
import com.example.QuizWebApp.Model.Login.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This configuration class contains the Spring bean configurations for the application.
 */
@Configuration
public class AppConfiguration {

    /**
     * Creates a new instance of the UserService, which handles user-related operations.
     *
     * @param userRepository  the repository for user data access
     * @param passwordEncoder the encoder used to hash user passwords
     * @return the newly created UserService instance
     */
    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }

    /**
     * Creates a new instance of the BCryptPasswordEncoder, which is used to encode/hashed user passwords.
     *
     * @return the newly created BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





