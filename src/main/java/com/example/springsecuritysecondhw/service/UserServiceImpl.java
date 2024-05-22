package com.example.springsecuritysecondhw.service;

import com.example.springsecuritysecondhw.dto.UserDto;
import com.example.springsecuritysecondhw.model.User;
import com.example.springsecuritysecondhw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.lang.System.*;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${user.maxFailedAttempts}")
    private int MAX_FAILED_ATTEMPTS;

    @Value("${user.periodOfBlocking}")
    private long PERIOD_OF_BLOCKING;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return convertUserToUserDto(userRepository.findByUsername(username).get());
    }

    @Override
    public UserDto getUserInfo(Long id) {
        if (existById(id)) {
            return convertUserToUserDto(findUserById(id));
        }
        return null;
    }

    @Override
    public boolean addUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            userRepository.save(convertUserDtoToUser(userDto));
            return true;
        }
        return false;
    }

    @Override
    public boolean editUser(Long id, UserDto userDto) {
        if (existById(id)) {
            User user = findUserById(id);
            if (userDto.getUsername() != null) {
                user.setUsername(userDto.getUsername());
            } else if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            } else if (userDto.getRole() != null) {
                user.setRole(userDto.getRole());
            } else if (user.equals(findUserById(id))) {
                return false;
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (existById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public void resetBlocking(Long id) {
        if (existById(id)) {
            User user = userRepository.findById(id).get();
            user.setAccountLocked(false);
            user.setFailedAttempts(0);
            userRepository.save(user);
        }
    }

    @Override
    public void checkBlocked(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user.isAccountLocked()) {
            if (user.getDateOfUnblocking().after(new Date())) {
                throw new LockedException(null);
            }
        }
        if (user == null || !encoder.matches(password, user.getPassword())) {
            if (user != null) {
                increaseFailedAttempts(user);
            }
            throw new BadCredentialsException(null);
        }

        resetBlocking(user.getId());
    }

    @Override
    public String getBlockedDate(String username) {
        return userRepository.findByUsername(username).get().getDateOfUnblocking().toString();
    }

    private void increaseFailedAttempts(User user) {
        int newFailedAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newFailedAttempts);
        if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setDateOfUnblocking(new Date(currentTimeMillis() + PERIOD_OF_BLOCKING));
        }
        userRepository.save(user);
    }


    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private User convertUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        user.setRole(userDto.getRole());
        return user;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    private boolean existById(Long id) {
        return userRepository.existsById(id);
    }

}
