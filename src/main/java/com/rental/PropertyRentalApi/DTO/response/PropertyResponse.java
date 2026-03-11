package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricityCost;
    private BigDecimal waterCost;
    private Boolean available;

    private CategoryResponse category;
    private UserResponse createdBy;
    private List<String> images;
    private Long reviewCount;
    private Long favoriteCount;
}
