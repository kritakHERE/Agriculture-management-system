package com.agrinepal.controller;

import com.agrinepal.model.User;
import com.agrinepal.service.AuthService;

import java.util.Optional;

public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public Optional<User> login(String username, String password) {
        return authService.login(username, password);
    }
}
