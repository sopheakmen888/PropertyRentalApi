package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CategoryUpdateRequest {
    @NotBlank
    private String name;
}
