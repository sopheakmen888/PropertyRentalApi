package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.PropertyCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.PropertyUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.PaginatedResponse;
import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.*;
import com.rental.PropertyRentalApi.Mapper.PropertyMapper;
import com.rental.PropertyRentalApi.Repository.*;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.PropertyService;
import com.rental.PropertyRentalApi.Utils.AuthUtil;
import com.rental.PropertyRentalApi.Utils.HelperFunction;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("unused")
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FavoritesRepository favoritesRepository;
    private final JwtService jwtService;
    private final PropertyMapper propertyMapper;
    private final AuthUtil authUtil;
    private final HelperFunction helperFunction;


    // ==============
    // SEARCH PROPERTIES BY MULTIPLE FILTERS
    // ==============


    @Override
        public PaginatedResponse<PropertyResponse> searchProperties1(String title, String description, String categoryName,
                int page, int size, String address, String propertyType) {
        
                Pageable pageable = PageRequest.of(page, size);
        
                Page<Properties> propertyPage = propertyRepository.searchProperties(
                        title != null ? title : "",
                        description != null ? description : "",
                        categoryName != null ? categoryName : "",
                        address != null ? address : "",
                        propertyType != null ? propertyType : "",
                        pageable
                );
        
                if (propertyPage.isEmpty()) {
                throw notFound("No properties found matching the search criteria.");
                }
        
                List<PropertyResponse> propertyResponses = propertyPage.getContent()
                        .stream()
                        .map(propertyMapper::toPropertyResponse)
                        .toList();
        
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
    // GET ALL WITH PAGINATION
    // ==============
    @Override
    public PaginatedResponse<PropertyResponse> getAll(int page, int size) {
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
                .map(propertyMapper::toPropertyResponse)
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

        return propertyMapper.toPropertyResponse(property);
    }

    // ==============
    // CREATE
    // ==============
    @Override
    public PropertyResponse create(
            PropertyCreateRequest request
    ) {

        // ==============
        // GET CURRENT USER
        // ==============
        Users currentUser = authUtil.getAuthenticatedUser();

        // ==============
        // GET CATEGORY
        // ==============
        Categories category = helperFunction.getCategoryOrThrow(request.getCategoryName());


        // ==============
        // MAP REQUEST TO ENTITY
        // ==============
        Properties property = propertyMapper.toPropertyEntity(request);

        // ==============
        // SET CREATED BY USER
        // ==============
        property.setCreatedBy(currentUser);
        property.setCategory(category);

        // ==============
        // SAVE AND RETURN NEW PROPERTY
        // ==============
        Properties savedProperty =  propertyRepository.save(property);

        return propertyMapper.toPropertyResponse(savedProperty);
    }

    // ==============
    // UPDATE
    // ==============
    @Override
    public PropertyResponse update(Long id, PropertyUpdateRequest request) {
        // ==============
        // GET CURRENT USER
        // ==============
        Users currentUser = authUtil.getAuthenticatedUser();

        // ==============
        // GET CATEGORY
        // ==============
        Categories category = helperFunction.getCategoryOrThrow(request.getCategoryName());

        // ==============
        // FIND PROPERTY TO UPDATE
        // ==============
        Properties property = propertyRepository.findById(id)
                .orElseThrow(() -> notFound("Property not found."));

        // ==============
        // CHECK PERMISSIONS
        // ==============
        if (!property.getCreatedBy().getId().equals(currentUser.getId())) {
            throw forbidden("You are not allowed to update this property");
        }

        // ==============
        // UPDATE PROPERTY DETAILS
        // ==============
        propertyMapper.updatePropertyEntity(request, property);

        property.setCategory(category);

        Properties updated = propertyRepository.save(property);

        return propertyMapper.toPropertyResponse(updated);
    }

    // ==============
    // DELETE
    // ==============
    @Override
    public void delete(Long id) {

        Users currentUser = authUtil.getAuthenticatedUser();

        Properties property = propertyRepository.findById(id)
                .orElseThrow(() -> notFound("Property not found."));

        if (!property.getCreatedBy()
                .getId()
                .equals(currentUser.getId())
        ) {

            throw forbidden(
                    "You are not allowed to delete this property"
            );
        }

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
        Users currentUser = authUtil.getAuthenticatedUser();

        // ==============
        // GET USER'S PROPERTIES
        // ==============
        List<Properties> propertiesByCurrentUser =
                propertyRepository.findAllByCreatedBy(currentUser);

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
                .map(propertyMapper::toPropertyResponse)
                .toList();
    }

    @Override
    public void addFavorite(Long propertyId, Long userId) {
        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyFavorite = favoritesRepository.existsByPropertyAndUser(property, user);
        if (alreadyFavorite) return;

        Favorites favorite = new Favorites();
        favorite.setProperty(property);
        favorite.setUser(user);
        favoritesRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long propertyId, Long userId) {
        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Favorites favorite = favoritesRepository.findByPropertyAndUser(property, user)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoritesRepository.delete(favorite);
    }

    @Override
    public PaginatedResponse<PropertyResponse> searchProperties(String title, String description, String categoryName,
                int page, int size, String address, String propertyType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProperties'");
    }
}