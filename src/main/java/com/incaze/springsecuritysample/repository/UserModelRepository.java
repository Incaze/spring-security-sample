package com.incaze.springsecuritysample.repository;

import com.incaze.springsecuritysample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModelRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
}