package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.response.*;
import com.rental.PropertyRentalApi.DTO.request.AuthRequest;
import com.rental.PropertyRentalApi.Entity.RefreshToken;
import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.RefreshTokenRepository;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        // ========================
        // GET REFRESH TOKEN FROM COOKIE
        // ========================
        String refreshTokenValue =
                cookieHelper.getCookieValue(request, "refreshToken");

        if (refreshTokenValue == null) {
            throw unauthorized("Refresh token missing");
        }

        // ========================
        // LOOK UP REFRESH TOKEN IN DATABASE
        // ========================
        RefreshToken oldToken = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> unauthorized("Invalid refresh token"));

        // ========================
        // VALIDATE REFRESH TOKEN
        // - Must not be revoked
        // - Must not be expired
        // ========================
        if (oldToken.isRevoked() || oldToken.getExpiresAt().isBefore(Instant.now())) {
            throw unauthorized("Refresh token expired or revoked");
        }

        // ========================
        // REUSE DETECTION (ROTATION)
        // Invalidate the old refresh token immediately
        // ========================
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // ========================
        // EXTRACT USER FROM TOKEN
        // ========================
        Users user = oldToken.getUsers();

        // ========================
        // GENERATE NEW ACCESS TOKEN (JWT)
        // ========================
        String newAccessToken = jwtService.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(Roles::getName)
                        .toList()
        );

        // ========================
        // CREATE NEW REFRESH TOKEN (SERVER-SIDE)
        // ========================
        String newRefreshTokenValue = UUID.randomUUID().toString();

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(newRefreshTokenValue);
        newToken.setUsers(user);
        newToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(newToken);

        // ========================
        // STORE NEW TOKENS IN HTTP-ONLY COOKIES
        // ========================
        cookieHelper.setAuthCookie(
                response,
                "accessToken",
                newAccessToken,
                300 // 5 minutes
        );

        cookieHelper.setAuthCookie(
                response,
                "refreshToken",
                newRefreshTokenValue,
                259200 // 30 days
        );

        // ========================
        // RETURN NEW ACCESS TOKEN
        // ========================
        return new RefreshTokenResponse(newAccessToken);
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
        Users user = mapperFunction.toUserEntity(request);

        // Set encoded password (can't be done by MapStruct)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ========================
        // ASSIGN ROLES TO USER
        // ========================
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<Roles> roles = roleRepository.findAllById(request.getRoles());
            if (roles.size() != request.getRoles().size()) {
                throw badRequest("Some roles not found.");
            }
            user.setRoles(new HashSet<>(roles));
        } else {
            Roles defaultRole = roleRepository.findByName("user")
                    .orElseThrow(() -> notFound("Default role not found."));
            user.setRoles(new HashSet<>(Set.of(defaultRole)));
        }

        // ========================
        // SAVE USER
        // ========================
        Users savedUser = userRepository.save(user);

        // ========================
        // EXTRACT ROLE NAMES
        // ========================
        // List<String> roles = mapperFunction.mapRolesToStringList(savedUser.getRoles());
        List<String> roles = user.getRoles()
                .stream()
                .map(Roles::getName)
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

        // ========================
        // DB BACKEND REFRESH TOKEN
        // ========================
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUsers(user);
        refreshToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);

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
                refreshTokenValue,
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

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> notFound("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw unauthorized("Invalid password");
        }

        // ========================
        // EXTRACT ROLE NAME
        // ========================
        List<String> roles = user.getRoles()
                .stream()
                .map(Roles::getName)
                .toList();

        // ========================
        // MAP USER TO RESPONSE USING MAPSTRUCT
        // ========================
        UserResponse userResponse = mapperFunction.toUserResponse(user);

        // ========================
        // GENERATE ACCESS TOKENS
        // ========================
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getUsername(),
                roles
        );

        // ========================
        // DB BACKEND REFRESH TOKEN
        // ========================
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setUsers(user);
        refreshToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);

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
                refreshTokenValue,
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
        String refreshTokenValue = cookieHelper.getCookieValue(request, "refreshToken");

        if (refreshTokenValue != null) {
            refreshTokenRepository.findByToken(refreshTokenValue)
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
        }

        cookieHelper.clearAuthCookie(response, "accessToken");
        cookieHelper.clearAuthCookie(response, "refreshToken");

        return new ApiResponse<>(false, "User logout successfully.");
    }
}