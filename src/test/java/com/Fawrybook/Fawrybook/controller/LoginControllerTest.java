package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.exceptions.InvalidCredentialsException;
import com.Fawrybook.Fawrybook.exceptions.ValidationException;
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
class LoginControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private Map<String, String> validRequest;
    private Map<String, String> emptyRequest;

    @BeforeEach
    void setUp() {
        validRequest = Map.of(
                "username", "testuser",
                "password", "password123"
        );

        emptyRequest = Map.of(
                "username", "",
                "password", ""
        );
    }

    @Test
    void login_ShouldReturnOkResponse_WhenUserExistsAndCredentialsAreCorrect() {
        // Arrange
        String expectedToken = "mockedToken";
        when(authService.authenticateUser(validRequest.get("username"), validRequest.get("password")))
                .thenReturn(expectedToken);

        // Act
        ResponseEntity<ApiResponse<Object>> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(true, response.getBody().isStatus());
        assertEquals("Login successful", response.getBody().getMessage());
        assertEquals(expectedToken, ((Map<?, ?>) response.getBody().getData()).get("token"));
    }

    @Test
    void login_ShouldReturnUnauthorizedResponse_WhenCredentialsAreIncorrect() {
        // Arrange
        when(authService.authenticateUser(validRequest.get("username"), validRequest.get("password")))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        // Act
        ResponseEntity<ApiResponse<Object>> response = authController.login(validRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        assertEquals(false, response.getBody().isStatus());
        assertEquals("Invalid username or password", response.getBody().getMessage());
    }

}
