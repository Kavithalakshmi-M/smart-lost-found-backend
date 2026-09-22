package com.smartlostfound.userservice.dto;

import com.smartlostfound.userservice.entity.User;

public class LoginResponse {

    private String token;
    private Long userId;
    private String name;
    private String email;
    private User.Role role;

    public LoginResponse(String token,
                         Long userId,
                         String name,
                         String email,
                         User.Role role) {

        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }
}