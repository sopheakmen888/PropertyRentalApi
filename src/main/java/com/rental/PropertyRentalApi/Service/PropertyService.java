package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;

import java.util.List;

public interface PropertyService {
        PaginatedResponse<PropertyResponse> getAll(int page, int size);

    PropertyResponse getById(Long id);
    PropertyResponse create(PropertyCreateRequest request);
    PropertyResponse update(Long id, PropertyUpdateRequest request);
    void delete(Long id);


    void addFavorite(Long propertyId, Long userId);

    void removeFavorite(Long propertyId, Long userId);

    List<PropertyResponse> getPropertiesByCurrentUser(Long userId);

    // ===== Search / Filter =====
    List<PropertyResponse> searchProperties(Long locationId, Long categoryId, Double minPrice, Double maxPrice);
}
