package com.smartlostfound.userservice.service;

import com.smartlostfound.userservice.dto.LoginRequest;
import com.smartlostfound.userservice.dto.LoginResponse;
import com.smartlostfound.userservice.dto.RegisterRequest;
import com.smartlostfound.userservice.dto.UserResponse;
import com.smartlostfound.userservice.entity.User;
import com.smartlostfound.userservice.repository.UserRepository;
import com.smartlostfound.userservice.security.JwtService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTER
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(User.Role.USER);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // LOGIN
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // GET ALL USERS
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET USER BY ID
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id));

        return convertToResponse(user);
    }

    // GET USER BY EMAIL
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email));

        return convertToResponse(user);
    }

    // ENTITY → DTO
    private UserResponse convertToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}