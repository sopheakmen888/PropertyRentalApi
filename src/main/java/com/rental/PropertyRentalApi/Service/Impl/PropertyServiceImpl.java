package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final JwtService jwtService;
    private final MapperFunction mapperFunction;

    // ==============
    // GET ALL WITH PAGINATION
    // ==============
    @Override
    public PaginatedResponse<PropertyResponse> getAll(int page, int size) {
        // Create pageable object (pages are 0-indexed)
        Pageable pageable = PageRequest.of(
                page,
                size
        );

        // Fetch paginated data
        Page<Properties> propertyPage = propertyRepository.findAll(pageable);

        // Check if page is empty
        if (propertyPage.isEmpty()) {
            throw notFound("Properties not found.");
        }

        // Map entities to responses
        List<PropertyResponse> propertyResponses = propertyPage.getContent()
                .stream()
                .map(mapperFunction::toPropertyResponse)
                .toList();

        // Build pagination metadata
        PaginatedResponse.PaginationMeta paginationMeta = new PaginatedResponse.PaginationMeta(
                propertyPage.getNumber() + 1,
                propertyPage.getSize(),
                propertyPage.getTotalElements(),
                propertyPage.getTotalPages(),
                propertyPage.hasNext(),
                propertyPage.hasPrevious()
        );

        return new PaginatedResponse<>(propertyResponses, paginationMeta);
    }

    // ==============
    // GET BY ID
    // ==============
    @Override
    public PropertyResponse getById(Long id) {

        Properties property = propertyRepository.findById(id)
                .orElseThrow(() -> notFound("Property not found."));

        return mapperFunction.toPropertyResponse(property);
    }

    // ==============
    // CREATE
    // ==============
    @Override
    public PropertyResponse create(PropertyCreateRequest request) {

        // ==============
        // GET CURRENT USER
        // ==============
        Users currentUser = jwtService.getCurrentUser();

        // ==============
        // CHECK IF USER EXIST
        // ==============
        if (currentUser == null) {
            throw unauthorized("Authentication required - no authenticated user found");
        }

        // ==============
        // MAP REQUEST TO ENTITY
        // ==============
        Properties property = mapperFunction.toPropertyEntity(request);

        // ==============
        // SET CREATED BY USER
        // ==============
        property.setCreatedBy(currentUser);

        // ==============
        // SAVE AND RETURN NEW PROPERTY
        // ==============
        Properties savedProperty =  propertyRepository.save(property);
        return mapperFunction.toPropertyResponse(savedProperty);
    }

    // ==============
    // UPDATE
    // ==============
    @Override
    public PropertyResponse update(Long id, PropertyUpdateRequest request) {
        // ==============
        // GET CURRENT USER
        // ==============
        Users currentUser = jwtService.getCurrentUser();

        // ==============
        // FIND PROPERTY TO UPDATE
        // ==============
        Properties findPropertyAndUpdate = propertyRepository.findById(id)
                .orElseThrow(() -> notFound("Property not found."));

        // ==============
        // CHECK PERMISSIONS
        // ==============
        if (!findPropertyAndUpdate.getCreatedBy().getId().equals(currentUser.getId())) {
            throw forbidden("You are not allowed to update this property");
        }

        // ==============
        // UPDATE PROPERTY DETAILS
        // ==============
        mapperFunction.updatePropertyEntity(request, findPropertyAndUpdate);

        return mapperFunction.toPropertyResponse(findPropertyAndUpdate);
    }

    // ==============
    // DELETE
    // ==============
    @Override
    public void delete(Long id) {
        Properties property = propertyRepository.findById(id)
                .orElseThrow(() -> notFound("Property not found."));
        propertyRepository.delete(property);
    }

    // ==============
    // GET BY CURRENT USER
    // ==============
    @Override
    public List<PropertyResponse> getPropertiesByCurrentUser() {
        // ==============
        // GET CURRENT USER
        // ==============
        Users currentUser = jwtService.getCurrentUser();

        // ==============
        // GET USER'S PROPERTIES
        // ==============
        List<Properties> propertiesByCurrentUser = propertyRepository.findAllByCreatedBy(currentUser);

        // ==============
        // CHECK IF USER HAS PROPERTIES
        // ==============
        if (propertiesByCurrentUser.isEmpty()) {
            throw notFound("You have not created any properties yet.");
        }

        // ==============
        // MAP TO RESPONSE DTO
        // ==============
        return propertiesByCurrentUser.stream()
                .map(mapperFunction::toPropertyResponse)
                .toList();
    }
}