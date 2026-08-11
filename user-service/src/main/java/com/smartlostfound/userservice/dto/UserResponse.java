package com.smartlostfound.userservice.dto;

import com.smartlostfound.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private User.Role role;
}