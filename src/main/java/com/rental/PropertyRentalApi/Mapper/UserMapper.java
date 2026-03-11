package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.RegisterRequest;
import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.UserResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Entity.UsersProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        config = MapperConfiguration.class,
        uses = { FavoriteMapper.class, RoleMapper.class }
)
public interface UserMapper {

    // ============
    // REQUESTS
    // ============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Users toUserEntity(RegisterRequest request);


    Users toUserEntity(UserCreateRequest request);

    void updateUserEntity(
            UserUpdateRequest request,
            @MappingTarget Users entity
    );

    // ============
    // RESPONSES
    // ============
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "profile", expression = "java(mapProfile(user.getProfile()))")
//    @Mapping(target = "favorites", ignore = true)
    UserResponse toUserResponse(Users user);

    // ============
    // PROFILE MAPPER
    // ============
    default String mapProfile(UsersProfile profile) {
        if (profile == null) return null;
        return profile.getUrls();
    }
}