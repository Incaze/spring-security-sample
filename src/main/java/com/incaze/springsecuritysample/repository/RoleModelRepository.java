package com.incaze.springsecuritysample.repository;

import com.incaze.springsecuritysample.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleModelRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}