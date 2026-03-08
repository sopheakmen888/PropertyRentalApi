package com.rental.PropertyRentalApi.DTO.request;

import com.rental.PropertyRentalApi.Entity.UsersProfile;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private UsersProfile profile;
}
