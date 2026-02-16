package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String username;
    private String dob;
    private String email;
    private String phone;
//    private String profilePhoto;
    private List<PropertyResponse> favorites;
    private List<String> roles;
}

