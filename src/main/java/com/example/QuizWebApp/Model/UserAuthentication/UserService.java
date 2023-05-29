package com.example.QuizWebApp.Model.UserAuthentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Service class for handling user-related operations.
 */
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserService with the given UserRepository and PasswordEncoder.
     *
     * @param userRepository  the UserRepository to use
     * @param passwordEncoder the PasswordEncoder to use
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the newly registered User
     */
    public User registerUser(String username, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        User savedUser = userRepository.save(newUser);
        log.info("Registered new user: {}", savedUser);
        return savedUser;
    }

    /**
     * Finds a user with the given username.
     *
     * @param username the username of the user to find
     * @return the User with the given username, or null if no such user exists
     */
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.debug("Found user by username '{}': {}", username, user);
        return user;
    }

    /**
     * Validates the given username and password.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return true if the username and password are valid, false otherwise
     */
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

