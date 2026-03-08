package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private UserResponse user;
    private Long propertyId;
    private String propertyTitle;
    private Instant createdAt;
    private Instant updatedAt;
}