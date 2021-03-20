package com.incaze.springsecuritysample.repository;

import com.incaze.springsecuritysample.model.Role;
import com.incaze.springsecuritysample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserModelRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
    List<User> findByRole(Role role);
}