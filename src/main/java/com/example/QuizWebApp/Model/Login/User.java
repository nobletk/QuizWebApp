package com.example.QuizWebApp.Model.Login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User entity class representing a user in the application.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    /**
     * Constructs a new instance of {@code User} with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

