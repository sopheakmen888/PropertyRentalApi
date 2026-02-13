package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LocationCreateRequest {
    @NotBlank
    private String name;
    private String description;
}
