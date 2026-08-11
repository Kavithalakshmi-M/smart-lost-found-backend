package com.smartlostfound.userservice.controller;

import com.smartlostfound.userservice.dto.LoginRequest;
import com.smartlostfound.userservice.dto.LoginResponse;
import com.smartlostfound.userservice.dto.RegisterRequest;
import com.smartlostfound.userservice.dto.UserResponse;
import com.smartlostfound.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse createdUser =
                userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // GET USER BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                userService.getUserByEmail(email)
        );
    }
}