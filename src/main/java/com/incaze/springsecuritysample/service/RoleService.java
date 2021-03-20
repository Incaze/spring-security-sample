package com.incaze.springsecuritysample.service;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";

    public static String getRoleWithDefaultPrefix(String role) {
        if (role == null) {
            return null;
        }
        role = role.toUpperCase();
        if (role.startsWith(DEFAULT_ROLE_PREFIX)) {
            return role;
        }
        return DEFAULT_ROLE_PREFIX + role;
    }
}
