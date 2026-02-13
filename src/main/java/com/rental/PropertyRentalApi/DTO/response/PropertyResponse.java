package com.rental.PropertyRentalApi.DTO.response;

import com.rental.PropertyRentalApi.Entity.Categories;
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

    private CategoryResponse categoryName;
    private UserResponse createdBy;
}
