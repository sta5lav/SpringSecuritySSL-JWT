package com.example.springsecuritysecondhw.dto;

import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String email;
    private Role role;

}
