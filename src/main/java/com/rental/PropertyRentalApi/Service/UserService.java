package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Users;

public interface UserService {
    PaginatedResponse<UserResponse> getAll(int page, int size);
//    List<UserResponse> getAll();

    UserResponse getById(Long id);
    UserResponse create(UserCreateRequest request);
    UserResponse update(Long id, UserUpdateRequest request);
    void delete(Long id);

    UserResponse userProfileInfo();
}
