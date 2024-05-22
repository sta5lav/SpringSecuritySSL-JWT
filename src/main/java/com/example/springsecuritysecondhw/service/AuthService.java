package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.AuthRequest;
import com.example.springsecuritysecondhw.dto.Register;

public interface AuthService {

    String auth(AuthRequest authRequest);

    void reg(Register register);
}
