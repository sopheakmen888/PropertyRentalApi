package com.rental.PropertyRentalApi.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyUpdateRequest {

<<<<<<< HEAD
    @NotBlank(message = "Title is required")
=======
>>>>>>> e4594a1 (add image favorite)
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

    private String categoryName;
=======
    private Long locationId;
    private Long categoryId;
    private List<String> images;
>>>>>>> e4594a1 (add image favorite)
}
