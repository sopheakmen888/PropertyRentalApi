package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.response.CategoryResponse;
import com.rental.PropertyRentalApi.Entity.Categories;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface CategoryMapper {

    // ============================
    // CATEGORY MAPPING
    // ============================
    CategoryResponse toCategoryResponse(Categories category);
}