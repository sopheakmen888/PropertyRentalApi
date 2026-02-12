package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.AuthResponse;
import com.rental.PropertyRentalApi.DTO.response.RegisterResponse;
import com.rental.PropertyRentalApi.Service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ========================
    // REFRESH ACCESS TOKEN
    // Uses refresh token from HTTP-only cookie
    // ========================
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(
                authService.refreshToken(request, response)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        RegisterResponse result = authService.register(request, httpRequest, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        AuthResponse result = authService.login(request, httpRequest, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }
}
