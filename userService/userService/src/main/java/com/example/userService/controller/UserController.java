package com.example.userService.controller;


import com.example.userService.entity.User;
import com.example.userService.model.request.AuthenticationResponse;
import com.example.userService.model.request.LoginRequest;
import com.example.userService.model.request.RegisterRequest;
import com.example.userService.repository.UserRepository;
import com.example.userService.response.UserResponse;
import com.example.userService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/service/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @Operation(summary = "Sign In to the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "login is successfully",
                    content = @Content(schema = @Schema(implementation = UserService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping( "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest,@RequestParam Map<String , Object> claims )  {
       return ResponseEntity.ok(userService.login(loginRequest));
    }



    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "registeratin is successfully",
                    content = @Content(schema = @Schema(implementation = UserService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(userService.register(registerRequest));
    }





    @Operation(summary = "Delete account user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = UserService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete")
    public UserResponse delete(@RequestParam("id") int id)
    {
       return userService.delete(id);
    }





    @Operation(summary = "Update account user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public UserResponse update(@RequestBody User user){
        return userService.update(user);
    }



    @Operation(summary = "get user by id")
    @GetMapping("/getUser")
    public Optional<User> getUserById(@RequestParam int id){
        return userService.findById(id);
    }

    @GetMapping("/getUserByEmail")
    public User getUserByEmail(@RequestParam String email){
        return userService.findByEmail(email);
    }


}