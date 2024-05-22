package com.example.springsecuritysecondhw.controller;


import com.example.springsecuritysecondhw.dto.AuthRequest;
import com.example.springsecuritysecondhw.dto.Register;
import com.example.springsecuritysecondhw.dto.JwtResponse;
import com.example.springsecuritysecondhw.exception.AuthError;
import com.example.springsecuritysecondhw.exception.RegisterError;
import com.example.springsecuritysecondhw.exception.UserAlreadyExistsException;
import com.example.springsecuritysecondhw.service.AuthService;
import com.example.springsecuritysecondhw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            userService.checkBlocked(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(authService.auth(authRequest)));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthError("Неверный логин или пароль",
                    HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        } catch (LockedException e) {
            return new ResponseEntity<>(new AuthError(String.format(
                    "Превышено количество попыток входа. Ваш аккаунт заблокирован до %s",
                    userService.getBlockedDate(authRequest.getUsername())
                    ),
                    HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        try {
            authService.reg(register);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new RegisterError("Данный пользователь уже зарегистрирован",
                    HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
        }
    }





}
