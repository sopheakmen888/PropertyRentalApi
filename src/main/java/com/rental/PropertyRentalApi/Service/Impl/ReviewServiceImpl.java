package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.ReviewCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.ReviewUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Reviews;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.ReviewRepository;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import com.rental.PropertyRentalApi.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final JwtService jwtService;
    private final MapperFunction mapper;

    // ==============
    // CREATE REVIEW
    // ==============
    @Override
    public ReviewResponse createReview(Long propertyId, ReviewCreateRequest request) {
        // Get current user
        Users currentUser = jwtService.getCurrentUser();

        // Find property
        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found."));

        // Check if user is trying to review their own property
        if (property.getCreatedBy().getId().equals(currentUser.getId())) {
            throw badRequest("You cannot review your own property.");
        }

        // Check if user already reviewed this property
        if (reviewRepository.existsByUserAndProperty(currentUser, property)) {
            throw badRequest("You have already reviewed this property.");
        }

        // Check if user has required role (assuming roles: "user", "tenant", "landlord")
        // Adjust role names based on your system
        boolean hasReviewRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("user") || role.getName().equals("tenant"));

        if (!hasReviewRole) {
            throw forbidden("You do not have permission to review properties.");
        }

        // Map request to entity
        Reviews review = mapper.toReviewEntity(request);
        review.setUser(currentUser);
        review.setProperty(property);

        // Save review
        Reviews savedReview = reviewRepository.save(review);

        return mapper.toReviewResponse(savedReview);
    }

    // ==============
    // UPDATE REVIEW
    // ==============
    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
        // Get current user
        Users currentUser = jwtService.getCurrentUser();

        // Find review
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found."));

        // Check if user owns this review
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw forbidden("You can only update your own reviews.");
        }

        // Update review
        mapper.updateReviewEntity(request, review);

        // Save updated review
        Reviews updatedReview = reviewRepository.save(review);

        return mapper.toReviewResponse(updatedReview);
    }

    // ==============
    // DELETE REVIEW
    // ==============
    @Override
    public void deleteReview(Long reviewId) {
        // Get current user
        Users currentUser = jwtService.getCurrentUser();

        // Find review
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found."));

        // Check if user owns this review
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw forbidden("You can only delete your own reviews.");
        }

        // Delete review
        reviewRepository.delete(review);
    }

    // ==============
    // GET REVIEWS BY PROPERTY
    // ==============
    @Override
    public Page<ReviewResponse> getReviewsByProperty(Long propertyId, int page, int size) {
        // Find property
        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found."));

        // Create pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Fetch reviews
        Page<Reviews> reviewPage = reviewRepository.findAllByProperty(property, pageable);

        // Map to response
        return reviewPage.map(mapper::toReviewResponse);
    }

    // ==============
    // GET CURRENT USER'S REVIEWS
    // ==============
    @Override
    public Page<ReviewResponse> getCurrentUserReviews(int page, int size) {
        // Get current user
        Users currentUser = jwtService.getCurrentUser();

        // Create pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Fetch user's reviews
        Page<Reviews> reviewPage = reviewRepository.findAllByUser(currentUser, pageable);

        // Map to response
        return reviewPage.map(mapper::toReviewResponse);
    }

    // ==============
    // GET REVIEW BY ID
    // ==============
    @Override
    public ReviewResponse getReviewById(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found."));

        return mapper.toReviewResponse(review);
    }
}