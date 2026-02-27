package com.rental.PropertyRentalApi.DTO.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricityCost;
    private BigDecimal waterCost;

    private CategoryResponse category;
    private List<String> images;
    private UserResponse createdBy;
    private Integer favoriteCount;
}
