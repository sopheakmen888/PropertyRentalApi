package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateRequest {
    private String fullname;
    private String phone;
}
