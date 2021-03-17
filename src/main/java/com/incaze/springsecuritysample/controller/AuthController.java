package com.incaze.springsecuritysample.controller;

import com.incaze.springsecuritysample.config.jwt.JwtProvider;
import com.incaze.springsecuritysample.controller.request.AuthRequest;
import com.incaze.springsecuritysample.controller.request.RegistrationRequest;
import com.incaze.springsecuritysample.controller.response.AuthResponse;
import com.incaze.springsecuritysample.model.User;
import com.incaze.springsecuritysample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = new User();
        try {
            userService.findByLogin(user.getLogin());
            return "User already exist";
        } catch (Exception e) {
        }
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());
        userService.saveUser(user);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }
}