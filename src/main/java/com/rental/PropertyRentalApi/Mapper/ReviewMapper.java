package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.request.ReviewCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.ReviewUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import com.rental.PropertyRentalApi.Entity.Reviews;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface ReviewMapper {

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

    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "propertyTitle", source = "property.title")
    ReviewResponse toReviewResponse(Reviews reviews);

}