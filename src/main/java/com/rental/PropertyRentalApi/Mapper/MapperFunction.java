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

    // ============================
    // PROPERTY CREATE
    // ============================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Properties toPropertyEntity(PropertyCreateRequest request);

    // ============================
    // PROPERTY UPDATE
    // ============================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "images", ignore = true) // map manually in service
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updatePropertyEntity(
            PropertyUpdateRequest request,
            @MappingTarget Properties entity
    );

    // ============================
    // PROPERTY RESPONSE
    // ============================
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(
            target = "categoryName",
            expression = "java(property.getCategoryName().getName())"
    )
    @Mapping(source = "images", target = "images")
    @Mapping(target = "favoriteCount", expression = "java(getFavoriteCount(property.getFavorites()))")
    PropertyResponse toPropertyResponse(Properties property);

    // ============================
    // IMAGES
    // ============================
    default List<String> mapImages(List<PropertyImages> images) {
        if (images == null) return List.of();
        return images.stream().map(PropertyImages::getImageUrl).toList();
    }

    default List<PropertyImages> mapStringsToImages(List<String> urls, Properties property) {
        if (urls == null) return List.of();
        return urls.stream().map(url -> {
            PropertyImages img = new PropertyImages();
            img.setImageUrl(url);
            img.setProperty(property);
            img.setPrimary(false);
            return img;
        }).toList();
    }

    // ============================
    // FAVORITES COUNT
    // ============================
    default Integer getFavoriteCount(List<Favorites> favorites) {
        return favorites == null ? 0 : favorites.size();
    }

    // ============================
    // USER MAPPING
    // ============================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Users toUserEntity(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Users toUserEntity(UserCreateRequest request);

    void updateUserEntity(UserUpdateRequest request, @MappingTarget Users entity);

    default UserResponse toUserResponse(Users user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullname(user.getFullname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRoles(mapRoles(user.getRoles()));
        return response;
    }

    default List<String> mapRoles(Set<Roles> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream().map(Roles::getName).toList();
    }

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
