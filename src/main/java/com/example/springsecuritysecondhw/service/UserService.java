package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.UserDto;

public interface UserService {

    UserDto getCurrentUser();
    UserDto getUserInfo(Long id);

    boolean addUser(UserDto userDto);

    boolean editUser(Long id, UserDto userDto);

    boolean deleteUser(Long id);

    void resetBlocking(Long id);

    void checkBlocked(String username, String password);

    String getBlockedDate(String username);


}
