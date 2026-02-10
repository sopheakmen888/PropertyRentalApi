package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.*;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Reviews;
import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MapperFunction {

    // ==============
    // PROPERTY REQUEST MAPPINGS
    // ==============
    Properties toPropertyEntity(PropertyCreateRequest request);

    void updatePropertyEntity(
            PropertyUpdateRequest request,
            @MappingTarget Properties entity
    );

    // ==============
    // PROPERTY RESPONSE MAPPINGS
    // ==============
    @Mapping(source = "createdBy", target = "createdBy")
    PropertyResponse toPropertyResponse(Properties property);

    // ==============
    // REGISTER REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Users toUserEntity(RegisterRequest request);


    // ==============
    // USER REQUEST MAPPINGS
    // ==============
    Users toUserEntity(UserCreateRequest request);

    void updateUserEntity(
            UserUpdateRequest request,
            @MappingTarget Users entity
    );

    // ==============
    // USER RESPONSE MAPPINGS
    // ==============
    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(Users user);

    // ==============
    // ROLES MAPPINGS
    // ==============
    default List<String> mapRoles(Set<Roles> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Roles::getName)
                .toList();
    }

    // ==============
    // REVIEW CREATE REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reviews toReviewEntity(ReviewCreateRequest request);

    // ==============
    // REVIEW UPDATE REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReviewEntity(ReviewUpdateRequest request, @MappingTarget Reviews entity);

    // ==============
    // REVIEW RESPONSE MAPPINGS
    // ==============
    @Mapping(source = "user", target = "user")
    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "property.title", target = "propertyTitle")
    ReviewResponse toReviewResponse(Reviews entity);
}