package com.example.QuizWebApp.Model.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@code User} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user with the given username.
     *
     * @param username the username of the user to find
     * @return the User with the given username, or null if no such user exists
     */
    User findByUsername(String username);
}
