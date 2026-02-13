package com.rental.PropertyRentalApi.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

<<<<<<< HEAD
//    @NotNull(message = "Category Name is required")
    @NotBlank(message = "Category Name is required")
    private String categoryName;

=======
    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private List<String> images; // optional
>>>>>>> e4594a1 (add image favorite)
}
