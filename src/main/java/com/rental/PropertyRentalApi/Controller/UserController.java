package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.UserMapper;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.UserService;
import com.rental.PropertyRentalApi.Utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // ==============
    // GET ALL USERS WITH PAGINATION
    // ==============
    @GetMapping("/admin/users")
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
    @GetMapping("/admin/users/{id}")
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
    @PostMapping("/admin/users")
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
    @PutMapping("/admin/users/{id}")
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
    @DeleteMapping("/admin/users/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {

        userService.delete(id);

        return new ApiResponse<>(
                200,
                true,
                "User deleted successfully.",
                null
        );
    }

    // =====================
    // GET AUTHENTICATED USER
    // =====================
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

    // =====================
    // UPDATE AUTHENTICATED USER PROFILE
    // =====================
    @PutMapping("/me/update-profile")
    public ApiResponse<UserResponse> updateProfile(
            @RequestBody UserUpdateRequest request
    ) {

        UserResponse response = userService.updateUserProfile(request);

        return new ApiResponse<>(
                200,
                true,
                "Profile updated successfully.",
                response
        );
    }
}
