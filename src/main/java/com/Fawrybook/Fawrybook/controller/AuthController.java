package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.exceptions.InvalidCredentialsException;
import com.Fawrybook.Fawrybook.exceptions.UserAlreadyExistsException;
import com.Fawrybook.Fawrybook.model.RevokedToken;
import com.Fawrybook.Fawrybook.repository.RevokedTokenRepository;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import com.Fawrybook.Fawrybook.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,RevokedTokenRepository revokedTokenRepository,JwtUtil jwtUtil) {
        this.authService = authService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String phone = request.get("phone");
            String token = authService.registerUser(request.get("username"), request.get("password"),request.get("phone"));

            Map<String, String> responseData = Map.of(
                    "username", username,
                    "phone", phone,
                    "token", token
            );
            ApiResponse<Map<String, String>> response = new ApiResponse<>(
                    true, HttpStatus.OK.value(), "User registered successfully", responseData
            );

            return ResponseEntity.ok(response);

        } catch (UserAlreadyExistsException e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    false, HttpStatus.CONFLICT.value(), e.getMessage(), null
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody Map<String, String> request) {
        try {
            String token = authService.authenticateUser(request.get("username"), request.get("password"));

            Map<String, String> responseData = Map.of(
                    "token", token,
                    "username", request.get("username")
            );

            ApiResponse<Object> response = new ApiResponse<>(
                    true, HttpStatus.OK.value(), "Login successful", responseData
            );

            return ResponseEntity.ok(response);

        } catch (ValidationException e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(
                    false, HttpStatus.BAD_REQUEST.value(), e.getMessage(), null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (InvalidCredentialsException e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(
                    false, HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(Map.of(
                    "status", 0,
                    "http_code", 400,
                    "message", "Invalid token format"
            ));
        }

        String jwtToken = token.substring(7);


        if (revokedTokenRepository.findByToken(jwtToken).isPresent()) {
            return ResponseEntity.status(400).body(Map.of(
                    "status", 0,
                    "http_code", 400,
                    "message", "Token already revoked"
            ));
        }

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(jwtToken);
        revokedToken.setRevokedAt(LocalDateTime.now());
        revokedTokenRepository.save(revokedToken);

        return ResponseEntity.ok(Map.of(
                "status", 1,
                "http_code", 200,
                "message", "Successfully logged out"
        ));
    }

    @PostMapping("/check-token")
    public ResponseEntity<?> isTokenExists(HttpServletRequest request, @RequestBody(required = false) Map<String, String> requestBody) {

        request.getHeaderNames().asIterator().forEachRemaining(header ->
        );


        String token = extractToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Invalid Token"));
        }

        boolean isValid = jwtUtil.validateToken(token);
        System.out.println("Token Validation Result: " + isValid);

        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    @PostMapping("/check-user")
    public ResponseEntity<?> isUserExists(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body(false);
        }

        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).body(false);
        }

        return ResponseEntity.ok(Map.of(
                "userId", userId
        ));

    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
