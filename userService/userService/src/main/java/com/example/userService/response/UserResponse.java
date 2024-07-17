package com.example.userService.response;

import com.example.userService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponse {
    private String message;
    private User data;

}
