package com.example.QuizWebApp.Model.Login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        User savedUser = userRepository.save(newUser);
        log.info("Registered new user: {}", savedUser);
        return savedUser;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.debug("Found user by username '{}': {}", username, user);
        return user;
    }

    public boolean validateUserCredentials(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            log.debug("User with username '{}' not found", username);
            return false;
        }
        boolean validCredentials = passwordEncoder.matches(password, user.getPassword());
        if (!validCredentials) {
            log.debug("Invalid credentials for user with username '{}'", username);
        }
        return validCredentials;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.password(user.getPassword());
        userBuilder.roles("USER");
        log.debug("Loaded user by username '{}': {}", username, userBuilder.build());
        return userBuilder.build();
    }
}

