package com.example.userService.services;

import com.example.userService.UserServiceApplication;
import com.example.userService.entity.Role;
import com.example.userService.entity.User;
import com.example.userService.model.request.AuthenticationResponse;
import com.example.userService.model.request.LoginRequest;
import com.example.userService.model.request.RegisterRequest;
import com.example.userService.response.UserResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void registerTest() {

        RegisterRequest request = RegisterRequest.builder()
                .email("sara357@gmail.com")
                .password("321")
                .role(Role.valueOf("USER"))
                .build();

        String url = "http://localhost:8080/service/user/register";
        ResponseEntity<RegisterRequest> response = restTemplate.postForEntity(url,request,RegisterRequest.class);

        assertEquals(response.getBody().getEmail(), request.getEmail());
    }

    @Test
    public void loginTest() {
        LoginRequest request = LoginRequest.builder()
                .email("sara177@gmail.com")
                .password("321")
                .build();
        String url = "http://localhost:8080/service/user/login";
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(url,request,AuthenticationResponse.class);

        assertEquals("login is successfully",response.getBody().getMessage());
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setId(52);
        user.setEmail("sara177@gmail.com");
        user.setPassword("321");
        user.setRole(Role.valueOf("USER"));

        String url = "http://localhost:8080/service/user/update";

        HttpEntity<User> requestEntity = new HttpEntity<>(user);
        ResponseEntity<UserResponse> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, UserResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("User updated succssfully", response.getBody().getMessage());
    }

    @Test
    public void deleteUserTest() {
        String url = "http://localhost:8080/service/user/delete?id=31";

        ResponseEntity<UserResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, null, UserResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("User deleted successfully", response.getBody().getMessage());

    }

}