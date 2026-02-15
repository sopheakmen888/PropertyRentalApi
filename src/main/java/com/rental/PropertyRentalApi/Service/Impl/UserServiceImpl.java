package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Service.UserService;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Utils.HelperFunction;

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
    private final HelperFunction helperFunction;
    private final MapperFunction mapperFunction;

    @Override
    public PaginatedResponse<UserResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Users> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent()
                .stream()
                .map(mapperFunction::toUserResponse)
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

        return mapperFunction.toUserResponse(user);
    }

    @Override
    public UserResponse create(UserCreateRequest request) {

        helperFunction.validateCreate(request);

        Users user = mapperFunction.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Users savedUser = userRepository.save(user);

        return mapperFunction.toUserResponse(savedUser);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found"));

        mapperFunction.updateUserEntity(request, user);

        Users updatedUser = userRepository.save(user);

        return mapperFunction.toUserResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> notFound("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse userProfileInfo() {

        Users currentUserData = helperFunction.getAuthenticatedUser();

        return mapperFunction.toUserResponse(currentUserData);
    }
}
