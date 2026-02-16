package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private int status;
    private Boolean success;
    private String message;
    private UserResponse user;
}
