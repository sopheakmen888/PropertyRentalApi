package com.rental.PropertyRentalApi.DTO.request;

import com.rental.PropertyRentalApi.Entity.UsersProfile;
import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateRequest {
    private String fullname;
    private String phone;
    private UsersProfile profile;
}
