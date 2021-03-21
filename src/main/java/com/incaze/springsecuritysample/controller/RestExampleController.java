package com.incaze.springsecuritysample.controller;

import com.incaze.springsecuritysample.config.jwt.JwtProvider;
import com.incaze.springsecuritysample.controller.request.AuthRequest;
import com.incaze.springsecuritysample.controller.request.DeleteRequest;
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

@RestController
public class RestExampleController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody @Valid RegistrationRequest request) throws UserAlreadyExistException {
        return userService.registerUser(request);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) throws UserNotFoundException {
        return userService.authUser(request);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody DeleteRequest request) throws UserNotFoundException {
        return userService.deleteUser(request);
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