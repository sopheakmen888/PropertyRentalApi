package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.DTO.request.ReviewCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.ReviewUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Reviews;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Mapper.MapperFunction;
import com.rental.PropertyRentalApi.Repository.PropertiesRepository;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertiesRepository propertyRepository;
    private final JwtService jwtService;
    private final MapperFunction mapper;

    // ======================
    // CREATE REVIEW
    // ======================
    @Override
    public ReviewResponse createReview(Long propertyId, ReviewCreateRequest request) {
        Users currentUser = jwtService.getCurrentUser();

        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found"));

        // Cannot review own property
        if (property.getCreatedBy().getId().equals(currentUser.getId())) {
            throw badRequest("You cannot review your own property.");
        }

        // Already reviewed
        if (reviewRepository.existsByUserAndProperty(currentUser, property)) {
            throw badRequest("You have already reviewed this property.");
        }

        // Check allowed roles (user or tenant)
        boolean allowedRole = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("user") || role.getName().equals("tenant"));
        if (!allowedRole) {
            throw forbidden("You do not have permission to review properties.");
        }

        // Map request to entity
        Reviews review = mapper.toReviewEntity(request);
        review.setUser(currentUser);
        review.setProperty(property);

        Reviews saved = reviewRepository.save(review);
        return mapper.toReviewResponse(saved);
    }

    // ======================
    // UPDATE REVIEW
    // ======================
    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
        Users currentUser = jwtService.getCurrentUser();

        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found"));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw forbidden("You can only update your own reviews.");
        }

        mapper.updateReviewEntity(request, review);
        Reviews updated = reviewRepository.save(review);

        return mapper.toReviewResponse(updated);
    }

    // ======================
    // DELETE REVIEW
    // ======================
    @Override
    public void deleteReview(Long reviewId) {
        Users currentUser = jwtService.getCurrentUser();

        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found"));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw forbidden("You can only delete your own reviews.");
        }

        reviewRepository.delete(review);
    }

    // ======================
    // GET REVIEWS BY PROPERTY
    // ======================
    @Override
    public Page<ReviewResponse> getReviewsByProperty(Long propertyId, int page, int size) {
        Properties property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviews = reviewRepository.findAllByProperty(property, pageable);

        return reviews.map(mapper::toReviewResponse);
    }

    // ======================
    // GET CURRENT USER REVIEWS
    // ======================
    @Override
    public Page<ReviewResponse> getCurrentUserReviews(int page, int size) {
        Users currentUser = jwtService.getCurrentUser();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviews = reviewRepository.findAllByUser(currentUser, pageable);

        return reviews.map(mapper::toReviewResponse);
    }

    // ======================
    // GET REVIEW BY ID
    // ======================
    @Override
    public ReviewResponse getReviewById(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> notFound("Review not found"));

        return mapper.toReviewResponse(review);
    }
}
