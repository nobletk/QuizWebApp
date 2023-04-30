package com.example.QuizWebApp.Model.Controller;

import com.example.QuizWebApp.Model.Login.User;
import com.example.QuizWebApp.Model.Login.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling user registration requests
 */
@Controller
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Display the registration form to the user
     *
     * @return the view name for the registration form
     */
    @GetMapping("/signup")
    public String showRegistrationForm() {
        log.info("Showing registration form");
        return "signup";
    }

    /**
     * Register a new user and redirect them to the home page
     *
     * @param username the desired username for the new user
     * @param password the desired password for the new user
     * @param model    the Spring MVC model object
     * @return the view name for the home page
     */
    @PostMapping("/signup")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            String errorMessage = "Username is already taken!";
            model.addAttribute("errorMessage", "Username is already taken!");
            log.warn(errorMessage);
            return "signup";
        }

        userService.registerUser(username, password);
        log.info("User registered: {}", username);
        return "redirect:/home";
    }
}
