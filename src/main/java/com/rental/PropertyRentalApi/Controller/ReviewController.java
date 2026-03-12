package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.ReviewCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.ReviewUpdateRequest;
import com.rental.PropertyRentalApi.DTO.response.ApiResponse;
import com.rental.PropertyRentalApi.DTO.response.ReviewResponse;
import com.rental.PropertyRentalApi.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@SuppressWarnings("unused")
public class ReviewController {

    private final ReviewService reviewService;

    // ==============
    // CREATE REVIEW FOR A PROPERTY
    // ==============
    @PostMapping("/property/{propertyId}")
    public ApiResponse<ReviewResponse> createReview(
            @PathVariable Long propertyId,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        ReviewResponse review = reviewService.createReview(propertyId, request);

        return new ApiResponse<>(
                201,
                true,
                "Review created successfully.",
                review
        );
    }

    // ==============
    // UPDATE REVIEW
    // ==============
    @PutMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request
    ) {
        ReviewResponse review = reviewService.updateReview(reviewId, request);

        return new ApiResponse<>(
                200,
                true,
                "Review updated successfully.",
                review
        );
    }

    // ==============
    // DELETE REVIEW
    // ==============
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);

        return new ApiResponse<>(
                204,
                true,
                "Review deleted successfully."
        );
    }

    // ==============
    // GET ALL REVIEWS FOR A PROPERTY
    // ==============
    @GetMapping("/property/{propertyId}")
    public ApiResponse<Page<ReviewResponse>> getReviewsByProperty(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewResponse> reviews = reviewService.getReviewsByProperty(propertyId, page, size);

        return new ApiResponse<>(
                200,
                true,
                "Reviews retrieved successfully.",
                reviews
        );
    }

    // ==============
    // GET CURRENT USER'S REVIEWS
    // ==============
    @GetMapping("/my-reviews")
    public ApiResponse<Page<ReviewResponse>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewResponse> reviews = reviewService.getCurrentUserReviews(page, size);

        return new ApiResponse<>(
                200,
                true,
                "Your reviews retrieved successfully.",
                reviews
        );
    }

    // ==============
    // GET REVIEW BY ID
    // ==============
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponse> getReviewById(@PathVariable Long reviewId) {
        ReviewResponse review = reviewService.getReviewById(reviewId);

        return new ApiResponse<>(
                200,
                true,
                "Review retrieved successfully.",
                review
        );
    }
}