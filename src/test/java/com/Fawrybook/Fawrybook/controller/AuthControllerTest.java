package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.exceptions.UserAlreadyExistsException;
import com.Fawrybook.Fawrybook.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private Map<String, String> request;

    @BeforeEach
    void setUp() {
        request = Map.of(
                "username", "testuser",
                "password", "password123",
                "phone", "1234567890"
        );
    }

    @Test
    void register_ShouldReturnOkResponse_WhenUserIsRegisteredSuccessfully() {
        // Arrange
        String expectedToken = "mockedToken";
        when(authService.registerUser(request.get("username"), request.get("password"), request.get("phone")))
                .thenReturn(expectedToken);

        // Act
        ResponseEntity<ApiResponse<?>> response = authController.register(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(true, response.getBody().isStatus());
        assertEquals("User registered successfully", response.getBody().getMessage());
        assertEquals(expectedToken, ((Map<?, ?>) response.getBody().getData()).get("token"));
    }

    @Test
    void register_ShouldReturnConflictResponse_WhenUsernameAlreadyExists() {
        // Arrange
        when(authService.registerUser(request.get("username"), request.get("password"), request.get("phone")))
                .thenThrow(new UserAlreadyExistsException("User with username " + request.get("username") + " already exists"));

        // Act
        ResponseEntity<ApiResponse<?>> response = authController.register(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCodeValue());
        assertEquals(false, response.getBody().isStatus());
        assertEquals("User with username testuser already exists", response.getBody().getMessage());
    }

    @Test
    void register_ShouldReturnConflictResponse_WhenPhoneAlreadyExists() {
        // Arrange
        when(authService.registerUser(request.get("username"), request.get("password"), request.get("phone")))
                .thenThrow(new UserAlreadyExistsException("User with phone " + request.get("phone") + " already exists"));

        // Act
        ResponseEntity<ApiResponse<?>> response = authController.register(request);

        // Assert
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCodeValue());
        assertEquals(false, response.getBody().isStatus());
        assertEquals("User with phone 1234567890 already exists", response.getBody().getMessage());
    }
}