package com.incaze.springsecuritysample.controller;

import com.incaze.springsecuritysample.config.jwt.JwtProvider;
import com.incaze.springsecuritysample.controller.request.AuthRequest;
import com.incaze.springsecuritysample.controller.request.RegistrationRequest;
import com.incaze.springsecuritysample.controller.response.AuthResponse;
import com.incaze.springsecuritysample.exception.UserAlreadyExistException;
import com.incaze.springsecuritysample.exception.UserNotFoundException;
import com.incaze.springsecuritysample.model.User;
import com.incaze.springsecuritysample.service.RoleService;
import com.incaze.springsecuritysample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws UserAlreadyExistException {
        String login = registrationRequest.getLogin();
        Optional<User> u = Optional.ofNullable(userService.findByLogin(login));
        if (u.isPresent()) {
            throw new UserAlreadyExistException(login);
        }
        
        User user = new User();
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(login);
        userService.saveUser(user);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) throws Exception {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
           throw new UserNotFoundException(request.getLogin());
        }

        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }

    @GetMapping("/admin/get")
    public String getAdmin() {
        return "Hi admin";
    }

    @GetMapping("/user/get")
    public String getUser() {
        return "Hi user";
    }

    @GetMapping("/get/{role}")
    @ResponseBody
    public List<User> getUserList(@PathVariable String role){
        role = RoleService.getRoleWithDefaultPrefix(role);
        return userService.findByRole(role);
    }

}