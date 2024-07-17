package com.example.userService.controller;

import com.example.userService.entity.Token;
import com.example.userService.repository.TokenRepository;
import com.example.userService.services.JwtService;
import com.example.userService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


@RestController
@RequestMapping("/service/token")
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);


    @Operation(summary = "verify expiration date of token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid",
                    content = @Content(schema = @Schema(implementation = UserService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/checkToken")
    public String checkToken(@RequestHeader("Authorization") String tokenstr) {
        try {
            Token token = tokenRepository.findByToken(tokenstr.substring(7));
            UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUser().getEmail());
            if (jwtService.isTokenValid(token.getToken(), userDetails)) {
                return "Token is valid";
            } else {
                return "Token is invalid";
            }
        } catch (NullPointerException e) {
            return "Token is invalid";
        } catch (Exception e) {
            return "Token is expired";
        }
    }

    @GetMapping("/getEmailByToken")
    @Operation(summary = "find token by user id", description = "find token by user id")
    public String getToken(@RequestHeader("Authorization") String tokenstr){
        Token token= tokenRepository.findByToken(tokenstr.substring(7));
        return token.getUser().getEmail();
    }




    @GetMapping("/getToken")
    @Operation(summary = "Find token by user ID", description = "token")
    public ResponseEntity<String> getToken(@RequestParam("id") int id) {
        String token = jwtService.getTokenById(id);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
    }
}