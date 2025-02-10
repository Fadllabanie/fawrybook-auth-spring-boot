package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.exceptions.InvalidCredentialsException;
import com.Fawrybook.Fawrybook.exceptions.UserAlreadyExistsException;
import com.Fawrybook.Fawrybook.model.User;
import com.Fawrybook.Fawrybook.repository.UserRepository;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import jakarta.validation.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String password, String phone) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists");
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new UserAlreadyExistsException("User with phone " + phone + " already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton("ROLE_USER"));
        user = userRepository.save(user);

        return jwtUtil.generateToken(username, user.getId());
    }

    public String authenticateUser(String username, String password) {
        // Validate input before making any database calls
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new ValidationException("Username and password cannot be empty");
        }

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return jwtUtil.generateToken(username, user.getId());
    }

}
