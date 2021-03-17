package com.incaze.springsecuritysample.controller;

import com.incaze.springsecuritysample.config.jwt.JwtProvider;
import com.incaze.springsecuritysample.controller.request.AuthRequest;
import com.incaze.springsecuritysample.controller.request.RegistrationRequest;
import com.incaze.springsecuritysample.controller.response.AuthResponse;
import com.incaze.springsecuritysample.model.User;
import com.incaze.springsecuritysample.service.UserService;
import jdk.jshell.spi.ExecutionControl;
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
        User check = userService.findByLogin(registrationRequest.getLogin());
        if (check != null) {
            return "User already exist";
        }
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());
        userService.saveUser(user);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) throws Exception {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
           throw new Exception("Invalid login or password");
        }
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }
}