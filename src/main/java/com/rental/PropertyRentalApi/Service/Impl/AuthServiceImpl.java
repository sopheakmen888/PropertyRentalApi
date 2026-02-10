package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.*;
import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.Entity.RoleEntity;
import com.rental.PropertyRentalApi.Entity.UserEntity;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.RoleRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.AuthService;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Utils.CookieHelper;


import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;
import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.badRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieHelper cookieHelper;
    private final RoleRepository roleRepository;
    private final MapperFunction mapperFunction;

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieHelper.getCookieValue(request, "refresh_token");

        if (refreshToken == null || refreshToken.isBlank()) {
            throw unauthorized("Refresh token is missing!");
        }

        if (!jwtService.validateToken(refreshToken)) {
            throw unauthorized("Invalid or expired refresh token.");
        }

        String userId = jwtService.extractUserId(refreshToken);
        String username = jwtService.extractUsername(refreshToken);

//        UserEntity user = userRepository.findById(Long.parseLong(userId))
//                .orElseThrow(() -> notFound("User not found."));
        UserEntity user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> notFound("User not found."));

        if (!user.getUsername().equals(username)) {
            throw unauthorized("Invalid refresh token.");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();

        return null;
    }

    @Override
    public RegisterResponse register(
            RegisterRequest request,
            HttpServletResponse response
    ) {
        // Validate username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw badRequest("Username already exists");
        }

        // Validate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email already exists");
        }

        // ========================
        // MAP REQUEST TO ENTITY USING MAPSTRUCT
        // ========================
        UserEntity user = mapperFunction.toUserEntity(request);

        // Set encoded password (can't be done by MapStruct)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ========================
        // ASSIGN ROLES TO USER
        // ========================
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<RoleEntity> roles = roleRepository.findAllById(request.getRoles());
            if (roles.size() != request.getRoles().size()) {
                throw badRequest("Some roles not found.");
            }
            user.setRoles(new HashSet<>(roles));
        } else {
            RoleEntity defaultRole = roleRepository.findByName("user")
                    .orElseThrow(() -> notFound("Default role not found."));
            user.setRoles(new HashSet<>(Set.of(defaultRole)));
        }

        // ========================
        // SAVE USER
        // ========================
        UserEntity savedUser = userRepository.save(user);

        // ========================
        // EXTRACT ROLE NAMES
        // ========================
        // List<String> roles = mapperFunction.mapRolesToStringList(savedUser.getRoles());
        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();

        // ========================
        // GENERATE TOKENS
        // ========================
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(savedUser.getId()),
                savedUser.getEmail(),
                savedUser.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(
                String.valueOf(savedUser.getId())
        );

        // ========================
        // SAVE TOKENS IN COOKIE
        // ========================
        cookieHelper.setAuthCookie(
                response,
                "accessToken",
                accessToken,
                900 // 15 minutes
        );

        cookieHelper.setAuthCookie(
                response,
                "refreshToken",
                refreshToken,
                259200 // 30 days
        );

        // ========================
        // MAP USER TO RESPONSE USING MAPSTRUCT
        // ========================
        UserResponse userResponse = mapperFunction.toUserResponse(savedUser);

        return new ApiResponse<>(
                201,
                true,
                "Register successfully.",
                new RegisterResponse(userResponse)
        ).getData();
    }

    @Override
    public AuthResponse login(
            AuthRequest request,
            HttpServletResponse response
    ) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> notFound("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw unauthorized("Invalid password");
        }

        // ========================
        // EXTRACT ROLE NAME
        // ========================
        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();

        // ========================
        // MAP USER TO RESPONSE USING MAPSTRUCT
        // ========================
        UserResponse userResponse = mapperFunction.toUserResponse(user);

        // ========================
        // GENERATE TOKENS
        // ========================
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(
                String.valueOf(user.getId())
        );

        // ========================
        // SAVE TOKENS IN COOKIE
        // ========================
        cookieHelper.setAuthCookie(
                response,
                "accessToken",
                accessToken,
                300 // 5 minutes
        );

        cookieHelper.setAuthCookie(
                response,
                "refreshToken",
                refreshToken,
                259200 // 30 days
        );

        // ========================
        // INCLUDE ACCESS TOKEN IN THE RESPONSE
        // ========================
        return new ApiResponse<>(
                200,
                true,
                "Login successfully",
                new AuthResponse(accessToken, userResponse)
        ).getData();
    }

    @Override
    public ApiResponse<Object> logout(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = cookieHelper.getCookieValue(request, "accessToken");
        String refreshToken = cookieHelper.getCookieValue(request, "refreshToken");

        cookieHelper.clearAuthCookie(response, "accessToken");
        cookieHelper.clearAuthCookie(response, "refreshToken");

        return new ApiResponse<>(false, "User logout successfully.");
    }
}