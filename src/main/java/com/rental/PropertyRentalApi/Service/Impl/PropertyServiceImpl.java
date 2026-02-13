package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.*;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.*;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertiesRepository propertiesRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final PropertyImagesRepository imagesRepository;
    private final FavoritesRepository favoritesRepository;
    private final MapperFunction mapperFunction;
    private final JwtService jwtService;

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

    @Override
    public PropertyResponse create(PropertyCreateRequest request, Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Locations location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Properties property = mapperFunction.toPropertyEntity(request);
        property.setCreatedBy(user);
        property.setLocation(location);
        property.setCategory(category);

        Properties savedProperty = propertiesRepository.save(property);

        // Save images
        if (request.getImages() != null) {
            request.getImages().forEach(url -> {
                PropertyImages image = new PropertyImages();
                image.setProperty(savedProperty);
                image.setImageUrl(url);
                image.setPrimary(false);
                imagesRepository.save(image);
            });
        }

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

    @Override
    public void addFavorite(Long propertyId, Long userId) {
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyFavorited = favoritesRepository.existsByPropertyAndUser(property, user);
        if (alreadyFavorited) return;

        Favorites favorite = new Favorites();
        favorite.setProperty(property);
        favorite.setUser(user);
        favoritesRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long propertyId, Long userId) {
        Properties property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Favorites favorite = favoritesRepository.findByPropertyAndUser(property, user)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoritesRepository.delete(favorite);
    }

    @Override
    public List<PropertyResponse> getPropertiesByCurrentUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return propertiesRepository.findAllByCreatedBy(user).stream()
                .map(mapper::toPropertyResponse)
                .toList();
    }

    // ======================
    // SEARCH / FILTER
    // ======================
    @Override
    public List<PropertyResponse> searchProperties(Long locationId, Long categoryId, Double minPrice, Double maxPrice) {

        List<Properties> properties;

        boolean hasLocation = locationId != null;
        boolean hasCategory = categoryId != null;
        boolean hasPrice = minPrice != null && maxPrice != null;

        if (hasLocation && hasCategory && hasPrice) {
            properties = propertiesRepository.findByLocationIdAndCategoryIdAndPriceBetween(locationId, categoryId, minPrice, maxPrice);
        } else if (hasLocation && hasCategory) {
            properties = propertiesRepository.findByLocationIdAndCategoryId(locationId, categoryId);
        } else if (hasLocation && hasPrice) {
            properties = propertiesRepository.findByLocationIdAndPriceBetween(locationId, minPrice, maxPrice);
        } else if (hasCategory && hasPrice) {
            properties = propertiesRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice);
        } else if (hasLocation) {
            properties = propertiesRepository.findByLocationId(locationId);
        } else if (hasCategory) {
            properties = propertiesRepository.findByCategoryId(categoryId);
        } else if (hasPrice) {
            properties = propertiesRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            properties = propertiesRepository.findAll();
        }

        return properties.stream()
                .map(mapper::toPropertyResponse)
                .toList();
    }
}
