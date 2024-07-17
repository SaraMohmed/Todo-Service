package com.example.userService.dto;

import com.example.userService.entity.Role;
import lombok.Data;

@Data
public class UserDto {

    private int id;
    private String email;
    private String password;

    private Role role;
}
