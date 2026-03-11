package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Service.PropertyService;
import com.rental.PropertyRentalApi.Utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SuppressWarnings("unused")
public class PropertyController {

    private final PropertyService propertyService;
    private final AuthUtil authUtil;
    
    // ==============
    // GET ALL WITH PAGINATION
    // ==============
    @GetMapping("/public/properties")
    public ApiResponse<PaginatedResponse<PropertyResponse>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Convert page to 0-indexed for service layer
        PaginatedResponse<PropertyResponse> paginatedProperties =
                propertyService.getAll(page, size);

        return new ApiResponse<>(
                200,
                true,
                "Get all properties successfully.",
                paginatedProperties
        );
    }

    // ========================
    // GET BY ID
    // ========================
    @GetMapping("/public/properties/{id}")
    public ApiResponse<PropertyResponse> getPropertyById(@PathVariable Long id) {
        PropertyResponse property = propertyService.getById(id);

        return new ApiResponse<>(
                200,
                true,
                "Get properties successfully.",
                property
        );
    }

    // ==============
    // CREATE
    // ==============
    @PostMapping("/properties")
    public ApiResponse<PropertyResponse> createdProperty(
            @Valid
            @RequestBody PropertyCreateRequest request
    ) {
        PropertyResponse property = propertyService.create(request);

        return new ApiResponse<>(
                201,
                true,
                "Created Property successfully.",
                property
        );
    }

    // ========================
    // UPDATE
    // ========================
    @PutMapping("/properties/{id}")
    public ApiResponse<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyUpdateRequest request
    ) {
        PropertyResponse updatedProperty = propertyService.update(id, request);

        return new ApiResponse<>(
                200,
                true,
                "Updated property successfully.",
                updatedProperty
        );
    }

    // ========================
    // DELETE
    // ========================
    @DeleteMapping("/properties/{id}")
    public ApiResponse<Void> deleteProperty(@PathVariable Long id) {

        propertyService.delete(id);

        return new ApiResponse<>(
                200,
                true,
                "Property deleted successfully."
        );
    }

    // ========================
    // GET CURRENT USER PROPERTIES
    // ========================
    @GetMapping("/my-properties")
    public ApiResponse<List<PropertyResponse>> getMyProperties() {

        List<PropertyResponse> properties =
                propertyService.getPropertiesByCurrentUser();

        return new ApiResponse<>(
                200,
                true,
                "Get your properties successfully.",
                properties
        );
    }

    // ============================
    // ADD FAVORITE
    // ============================
    @PostMapping("/properties/{propertyId}/favorite")
    public ApiResponse<Void> addFavorite(@PathVariable Long propertyId) {

        Users currentUser = authUtil.getAuthenticatedUser();

        propertyService.addFavorite(propertyId, currentUser.getId());

        return new ApiResponse<>(
                200,
                true,
                "Property added to favorites."
        );
    }

    // ============================
    // REMOVE FAVORITE
    // ============================
    @DeleteMapping("/properties/{propertyId}/favorite")
    public ApiResponse<Void> removeFavorite(@PathVariable Long propertyId) {
        Users currentUser = authUtil.getAuthenticatedUser()
                ;
        propertyService.removeFavorite(propertyId, currentUser.getId());

        return new ApiResponse<>(
                200,
                true,
                "Property removed from favorites."
        );
    }

    // ==============
    // SEARCH PROPERTIES BY MULTIPLE FILTERS
    // ==============
    @GetMapping("/public/properties/search")
    public ApiResponse<PaginatedResponse<PropertyResponse>> searchProperties(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String propertyType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long communeId,
            @RequestParam(required = false) Boolean available,

            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PaginatedResponse<PropertyResponse> paginatedProperties =
                propertyService.searchProperties(
                        title,
                        description,
                        categoryName,
                        address,
                        propertyType,
                        page, size,
                        provinceId,
                        districtId,
                        communeId,
                        available,
                        sortBy, sortDir
                );

        String message =
                paginatedProperties.getItems().isEmpty()
                        ? "No properties found."
                        : "Properties retrieved successfully.";

        return new ApiResponse<>(
                200,
                true,
                message,
                paginatedProperties
        );
    }
}