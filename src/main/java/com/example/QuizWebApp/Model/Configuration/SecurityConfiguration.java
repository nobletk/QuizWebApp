package com.example.QuizWebApp.Model.Configuration;

import com.example.QuizWebApp.Model.Login.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class configures Spring Security for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of {@link SecurityConfiguration}.
     *
     * @param userService     the user service to use for authentication
     * @param passwordEncoder the password encoder to use for encoding and verifying passwords
     */
    public SecurityConfiguration(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Configures the authentication manager to use the user service and password encoder for authentication.
     *
     * @param auth the authentication manager builder
     * @throws Exception if an error occurs while configuring the authentication manager
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the http security object to configure
     * @return the configured security filter chain
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/signup", "/login", "/static/**", "/Images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();
    }
}