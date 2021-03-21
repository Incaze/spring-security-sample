package com.incaze.springsecuritysample.service;

import com.incaze.springsecuritysample.config.jwt.JwtProvider;
import com.incaze.springsecuritysample.controller.request.AuthRequest;
import com.incaze.springsecuritysample.controller.request.DeleteRequest;
import com.incaze.springsecuritysample.controller.request.RegistrationRequest;
import com.incaze.springsecuritysample.controller.response.AuthResponse;
import com.incaze.springsecuritysample.exception.UserAlreadyExistException;
import com.incaze.springsecuritysample.exception.UserNotFoundException;
import com.incaze.springsecuritysample.model.Role;
import com.incaze.springsecuritysample.model.User;
import com.incaze.springsecuritysample.repository.RoleModelRepository;
import com.incaze.springsecuritysample.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private RoleModelRepository roleModelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public User registerUser(RegistrationRequest request) throws UserAlreadyExistException {
        String login = request.getLogin();
        Optional<User> u = Optional.ofNullable(this.findByLogin(login));
        if (u.isPresent()) {
            throw new UserAlreadyExistException(login);
        }

        User user = new User();
        user.setPassword(request.getPassword());
        user.setLogin(login);
        return this.save(user);
    }

    public AuthResponse authUser(AuthRequest request) throws UserNotFoundException {
        User user = this.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
            throw new UserNotFoundException(request.getLogin());
        }

        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }

    public String deleteUser(DeleteRequest request) throws UserNotFoundException {
        Optional<User> user = Optional.ofNullable(this.findByLogin(request.getLogin()));
        if (user.isEmpty()) {
            throw new UserNotFoundException(request.getLogin());
        }

        userModelRepository.delete(user.get());
        return "OK";
    }

    public User findByLogin(String login) {
        return userModelRepository.findByLogin(login);
    }

    public List<User> findByRole(String name) {
        Role role = roleModelRepository.findByName(name);
        return userModelRepository.findByRole(role);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    private User save(User user) {
        Role userRole = roleModelRepository.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userModelRepository.save(user);

        return user;
    }

}