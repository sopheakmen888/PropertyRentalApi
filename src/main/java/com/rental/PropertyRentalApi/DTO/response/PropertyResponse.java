package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    private Long id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricCost;
    private BigDecimal waterCost;

    private UserResponse createdBy;
}
