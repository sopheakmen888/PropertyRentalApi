package com.rental.PropertyRentalApi.Mapper;

import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.Favorites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfiguration.class, uses = UserMapper.class)
public interface FavoriteMapper {

    @Mapping(target = ".", source = "property")
    PropertyResponse toPropertyResponse(Favorites favorite);

    default List<PropertyResponse> mapFavorites(List<Favorites> favorites) {
        if (favorites == null) {
            return List.of();
        }
        return favorites.stream()
                .map(this::toPropertyResponse)
                .toList();
    }
}