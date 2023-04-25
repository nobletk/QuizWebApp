package com.example.QuizWebApp.Model.Controller;

import com.example.QuizWebApp.Model.Login.User;
import com.example.QuizWebApp.Model.Login.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String showRegistrationForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            model.addAttribute("errorMessage", "Username is already taken. Please choose another one.");
            return "signup";
        }

        userService.registerUser(username, password);
        return "redirect:/login"; //TODO redirect to home page
    }
}
