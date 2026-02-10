package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    // ==============
    // GET ALL WITH PAGINATION
    // ==============
    @GetMapping
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
    @GetMapping("/{id}")
    public ApiResponse<PropertyResponse> getPropertById(@PathVariable Long id) {
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
    @PostMapping
    public ApiResponse<PropertyResponse> createdProperty(
            @Valid
            @RequestBody PropertyCreateRequest request) {
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
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProperty(@PathVariable Long id) {

        propertyService.delete(id);

        return new ApiResponse<>(
                204,
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
}
