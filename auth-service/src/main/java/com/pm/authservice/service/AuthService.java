package com.pm.authservice.service;

import com.pm.authservice.dto.login.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService  {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Implement authentication logic here using loginDto
    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {

        log.info("Login request received: {}", loginRequestDTO.getPassword());
        log.info("Login request received: {}", loginRequestDTO);

        return userService.findByEmail(loginRequestDTO.getEmail()).filter(u -> {
            log.info("User password (hashed): {}", u.getPassword());
            return passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword());
        })
                 .<String>map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
    }

    public boolean validateToken(String substring) {
        try {
            return jwtUtil.validateToken(substring);
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }
}
