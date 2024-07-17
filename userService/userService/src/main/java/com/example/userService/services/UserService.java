package com.example.userService.services;

import com.example.userService.entity.User;
import com.example.userService.model.request.AuthenticationResponse;
import com.example.userService.model.request.LoginRequest;
import com.example.userService.model.request.RegisterRequest;
import com.example.userService.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
    UserResponse delete (int id);
    UserResponse update(User user);

    User findByEmail(String email);

    Optional<User> findById(int id);
}
