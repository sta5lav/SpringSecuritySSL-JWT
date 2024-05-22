package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.model.MyPrincipalUser;
import com.example.springsecuritysecondhw.repository.UserRepository;
import com.example.springsecuritysecondhw.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }
        return new MyPrincipalUser(user);
    }
}

