package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.forbidden;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtService jwtService;

    // -----------------------------
    // Helper: Check Admin or Agent
    // -----------------------------
    private void checkAdminOrAgent(Users user) {
        boolean allowed = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("admin") ||
                                  role.getName().equalsIgnoreCase("agent"));
        if (!allowed) {
            throw forbidden("Only Admin or Agent can perform this action.");
        }
    }


    // ============================
    // GET ALL PROPERTIES
    // ============================
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

    // ============================
    // GET PROPERTY BY ID
    // ============================
    @GetMapping("/public/properties/{id}")
    public ApiResponse<PropertyResponse> getPropertyById(@PathVariable Long id) {
        PropertyResponse property = propertyService.getById(id);
        return new ApiResponse<>(
                200,
                true,
                "Property retrieved successfully.",
                property
        );
    }

    // ============================
    // CREATE PROPERTY
    // ============================
    @PostMapping("/properties")
    public ApiResponse<PropertyResponse> createProperty(
            @Valid @RequestBody PropertyCreateRequest request) {

        Users currentUser = jwtService.getCurrentUser();
        checkAdminOrAgent(currentUser);

        PropertyResponse property = propertyService.create(request, currentUser.getId());

        return new ApiResponse<>(
                201,
                true,
                "Created Property successfully.",
                property
        );
    }

    // ============================
    // UPDATE PROPERTY
    // ============================
    @PutMapping("/properties/{id}")
    public ApiResponse<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyUpdateRequest request) {

        Users currentUser = jwtService.getCurrentUser();
        checkAdminOrAgent(currentUser);

        PropertyResponse updatedProperty = propertyService.update(id, request);

        return new ApiResponse<>(
                200,
                true,
                "Updated Property successfully.",
                updatedProperty
        );
    }

    // ============================
    // DELETE PROPERTY
    // ============================
    @DeleteMapping("/properties/{id}")
    public ApiResponse<Void> deleteProperty(@PathVariable Long id) {
        Users currentUser = jwtService.getCurrentUser();
        checkAdminOrAgent(currentUser);

        propertyService.delete(id);

        return new ApiResponse<>(
                204,
                true,
                "Property deleted successfully."
        );
    }

    // ============================
    // SEARCH & FILTER PROPERTIES
    // Example: /api/properties/search?locationId=1&categoryId=2&minPrice=100&maxPrice=200
    // ============================
    @GetMapping("/public/properties/search")
    public ApiResponse<List<PropertyResponse>> searchProperties(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<PropertyResponse> results = propertyService.searchProperties(locationId, categoryId, minPrice, maxPrice);
        return new ApiResponse<>(
                200,
                true,
                "Search results retrieved successfully.",
                results
        );
    }

    // ============================
    // GET CURRENT USER PROPERTIES
    // ============================
    @GetMapping("/my-properties")
    public ApiResponse<List<PropertyResponse>> getMyProperties() {
        Users currentUser = jwtService.getCurrentUser();
        List<PropertyResponse> properties = propertyService.getPropertiesByCurrentUser(currentUser.getId());
        return new ApiResponse<>(
                200,
                true,
                "Your properties retrieved successfully.",
                properties
        );
    }

    // ============================
    // ADD FAVORITE
    // ============================
    @PostMapping("/properties/{id}/favorite")
    public ApiResponse<Void> addFavorite(@PathVariable Long id) {
        Users currentUser = jwtService.getCurrentUser();
        propertyService.addFavorite(id, currentUser.getId());
        return new ApiResponse<>(
                200,
                true,
                "Property added to favorites."
        );
    }

    // ============================
    // REMOVE FAVORITE
    // ============================
    @DeleteMapping("/properties/{id}/favorite")
    public ApiResponse<Void> removeFavorite(@PathVariable Long id) {
        Users currentUser = jwtService.getCurrentUser();
        propertyService.removeFavorite(id, currentUser.getId());
        return new ApiResponse<>(
                200, true,
                "Property removed from favorites."
        );
    }
}
