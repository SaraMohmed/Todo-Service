package com.example.userService.services;

import com.example.userService.entity.Token;
import com.example.userService.entity.TokenType;
import com.example.userService.entity.User;
import com.example.userService.model.request.AuthenticationResponse;
import com.example.userService.model.request.LoginRequest;
import com.example.userService.model.request.RegisterRequest;
import com.example.userService.repository.TokenRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.response.UserResponse;
import com.example.userService.utils.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Override
//    public AuthenticationResponse login(LoginRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//        User user = userRepository.findUserByEmail(request.getEmail());
//        Map<String, Object> extraClaims = new HashMap<>();
//
//        String jwtToken = jwtService.createToken(user, extraClaims);
////        revokeAllTokenByUser(user);
//        saveUserToken(user, jwtToken);
//
//        return new AuthenticationResponse("login is successfully",jwtToken, request.getEmail());
//    }


    @Override
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findUserByEmail(request.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();

        String jwtToken = jwtService.createToken(user, extraClaims);

        Optional<Token> existingToken = tokenRepository.findByUser(user);

        if (existingToken.isPresent()) {

            updateUserToken(existingToken.get().getId(), user, jwtToken);
        } else {

            throw new EntityNotFoundException("No existing token found for the user, and token creation is not handled.");
        }

        return new AuthenticationResponse("login is successfully", jwtToken, request.getEmail());
    }

    private void updateUserToken(int id, User user, String jwtToken) {
        Optional<Token> optionalToken = tokenRepository.findById(id);

        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            token.setUser(user);
            token.setToken(jwtToken);
            token.setTokenType(TokenType.BEARER);
            token.setExpired(false);
            token.setRevoked(false);

            tokenRepository.save(token);
        } else {

            throw new EntityNotFoundException("Token with ID " + id + " not found");
        }
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enable(false)
                .build();

        if(!EmailValidator.isValidEmail(request.getEmail())){
            return new AuthenticationResponse("Email not valid",null,null);
        }
        else if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return new AuthenticationResponse("Email already existed , please sign in",null,null);
        }
        else{
            User savedUser = userRepository.save(user);
            Map<String, Object> extraClaims = new HashMap<>();
            String jwtToken = jwtService.createToken(user, extraClaims);
            saveUserToken(savedUser, jwtToken);
            return new AuthenticationResponse("registeratin is successfully",jwtToken, request.getEmail());
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }



    @Override
    public UserResponse delete(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.deleteById(id);
            return new UserResponse("User deleted successfully", null);
        } else {
            return new UserResponse("User not found ", null);
        }
    }


    @Override
    public UserResponse update(User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()){
            User user2 = User.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(user.getRole())
                    .build();
            User updateUser = userRepository.save(user2);
            return new UserResponse("User updated succssfully", updateUser);
        } else {
            return new UserResponse("User not found", null);
        }

    }




    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

}

