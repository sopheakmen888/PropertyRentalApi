package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.UploadsImages;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface PropertyMapper {

    // ============
    // REQUESTS
    // ============
    @Mapping(target = "category", ignore = true)
    Properties toPropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "category", ignore = true)
    void updatePropertyEntity(
            PropertyUpdateRequest request,
            @MappingTarget Properties entity
    );

    // ============
    // RESPONSES
    // ============
    @Mapping(source = "category", target = "category")
    @Mapping(target = "images", expression = "java(mapImages(property.getImages()))")
    @Mapping(target = "createdBy", source = "createdBy")
    PropertyResponse toPropertyResponse(Properties property);

    // ============
    // HELPERS
    // ============
    default List<String> mapImages(List<UploadsImages> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .map(image -> {
                    String url = image.getUrls();
                    if (url == null || url.isBlank()) {
                        return null;
                    }

                    // Cloudinary or external URL
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return url;
                    }

                    // Local file
                    return "/uploads/" + url;
                })
                .toList();
    }
}