package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.UserMapper;
import com.rental.PropertyRentalApi.Service.UserService;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Utils.AuthUtil;

import com.rental.PropertyRentalApi.Utils.UserValidatorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidatorUtil userValidator;
    private final AuthUtil authUtil;
    private final UserMapper userMapper;

    @Override
    public PaginatedResponse<UserResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Users> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();

        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        userPage.getNumber() + 1,
                        userPage.getSize(),
                        userPage.getTotalElements(),
                        userPage.getTotalPages(),
                        userPage.hasNext(),
                        userPage.hasPrevious()
                );

        return new PaginatedResponse<>(userResponses, paginationMeta);
    }

    @Override
    public UserResponse getById(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found"));


        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse create(UserCreateRequest request) {

        userValidator.validateCreate(request);

        Users user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Users savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found"));

        userValidator.validateUpdate(request);

        userMapper.updateUserEntity(request, user);

        Users updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse userProfileInfo() {

        Users authUser = authUtil.getAuthenticatedUser();

        Users currentUserData = userRepository
                .findById(authUser.getId())
                .orElseThrow(() -> notFound("User not found."));

        return userMapper.toUserResponse(currentUserData);
    }

    @Override
    public UserResponse updateUserProfile(UserUpdateRequest request) {

        // Get authenticated user
        Users authUser = authUtil.getAuthenticatedUser();

        // Map update fields
        userMapper.updateUserEntity(request, authUser);

        // Save
        Users updatedUser = userRepository.save(authUser);

        return userMapper.toUserResponse(updatedUser);
    }
}
