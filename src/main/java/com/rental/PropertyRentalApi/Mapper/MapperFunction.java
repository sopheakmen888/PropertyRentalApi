package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.*;
import com.rental.PropertyRentalApi.DTO.response.*;
import com.rental.PropertyRentalApi.Entity.*;
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
    @Mapping(target = "category", ignore = true)
    Properties toPropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "category", ignore = true)
    void updatePropertyEntity(
            PropertyUpdateRequest request,
            @MappingTarget Properties entity
    );

    // ==============
    // PROPERTY RESPONSE MAPPINGS
    // ==============
//    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "category", target = "category")
//    @Mapping(source = "images", target = "images")
    PropertyResponse toPropertyResponse(Properties property);

    // ==============
    // REGISTER REQUEST MAPPINGS
    // ==============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "profilePhoto", ignore = true)
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

    // ============================
    // CATEGORY MAPPING
    // ============================
    CategoryResponse toCategoryResponse(Categories category);

    // ============================
    // REVIEW MAPPING
    // ============================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reviews toReviewEntity(ReviewCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReviewEntity(ReviewUpdateRequest request, @MappingTarget Reviews entity);

    default ReviewResponse toReviewResponse(Reviews review) {
        if (review == null) return null;
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        response.setUser(toUserResponse(review.getUser()));
        if (review.getProperty() != null) {
            response.setPropertyId(review.getProperty().getId());
            response.setPropertyTitle(review.getProperty().getTitle());
        }
        return response;
    }
}
