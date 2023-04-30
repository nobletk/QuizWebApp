package com.example.QuizWebApp.Model.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * Custom authentication failure handler that sets an error message in the session and redirects to the login page
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Sets an error message in the session and redirects to the login page on authentication failure
     *
     * @param request   the HTTP request
     * @param response  the HTTP response
     * @param exception the authentication exception that occurred
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        request.getSession().setAttribute("error", "Invalid username or password");
        response.sendRedirect("/login");
    }
}

