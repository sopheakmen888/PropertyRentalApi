package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyUpdateRequest {

    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private BigDecimal electricityCost;
    private BigDecimal waterCost;
    private Long locationId;
    private Long categoryId;
    private List<String> images;
}
