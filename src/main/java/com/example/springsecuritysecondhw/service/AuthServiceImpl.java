package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.AuthRequest;
import com.example.springsecuritysecondhw.dto.Register;
import com.example.springsecuritysecondhw.exception.UserAlreadyExistsException;
import com.example.springsecuritysecondhw.model.User;
import com.example.springsecuritysecondhw.repository.UserRepository;
import com.example.springsecuritysecondhw.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;



    @Override
    public String auth(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return JwtUtil.generateToken(userDetails);
    }

    @Override
    public void reg(Register register) {
        if (userRepository.findByUsername(register.getUsername()).orElse(null) != null) {
            throw new UserAlreadyExistsException(null);
        }
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setEmail(register.getEmail());
        userRepository.save(user);
    }
}
