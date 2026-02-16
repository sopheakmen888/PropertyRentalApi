package com.rental.PropertyRentalApi.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private BigDecimal electricityCost;
    private BigDecimal waterCost;

//    @NotNull(message = "Category Name is required")
    @NotBlank(message = "Category Name is required")
    private String categoryName;

}
