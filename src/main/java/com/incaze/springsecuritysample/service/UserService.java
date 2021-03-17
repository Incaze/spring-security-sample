package com.incaze.springsecuritysample.service;

import com.incaze.springsecuritysample.model.Role;
import com.incaze.springsecuritysample.model.User;
import com.incaze.springsecuritysample.repository.RoleModelRepository;
import com.incaze.springsecuritysample.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserModelRepository userModelRepository;
    @Autowired
    private  RoleModelRepository roleModelRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        Role userRole = roleModelRepository.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userModelRepository.save(user);
    }

    public User findByLogin(String login) {
        return userModelRepository.findByLogin(login);
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
}