package com.rental.PropertyRentalApi.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyUpdateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private BigDecimal electricityCost;
    private BigDecimal waterCost;

    @NotBlank(message = "Category Name is required")
    private String categoryName;

//    @NotBlank(message = "Image is required")
    private List<String> images;
}
