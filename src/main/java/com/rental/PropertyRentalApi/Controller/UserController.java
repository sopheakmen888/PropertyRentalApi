package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // ==============
    // GET ALL USERS WITH PAGINATION
    // ==============
    @GetMapping
    public ApiResponse<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginatedResponse<UserResponse> paginatedUsers = userService.getAll(page, size);
        return new ApiResponse<>(
                200,
                true,
                "Get users successfully.",
                paginatedUsers
        );
    }


    // =====================
    // GET USER BY ID
    // =====================
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse getUserId = userService.getById(id);

        return new ApiResponse<>(
                200,
                true,
                "Get user successfully.",
                getUserId
        );
    }

    // =====================
    // CREATE USER
    // =====================
    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @RequestBody UserCreateRequest request
    ) {
        UserResponse createdUser = userService.create(request);

        return new ApiResponse<>(
                201,
                true,
                "User created successfully.",
                createdUser
        );
    }

    // =====================
    // UPDATE USER (PROFILE ONLY)
    // =====================
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
    ) {
        UserResponse updatedUser = userService.update(id, request);

        return new ApiResponse<>(
                200,
                true,
                "User updated successfully.",
                updatedUser
        );
    }

    // =====================
    // DELETE USER
    // =====================
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {

        userService.delete(id);

        return new ApiResponse<>(
                200,
                true,
                "User deleted successfully.",
                null
        );
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile() {

        UserResponse userData = userService.userProfileInfo();

        return new ApiResponse<>(
                200,
                true,
                "Get user info data successfully.",
                userData
        );
    }
}
