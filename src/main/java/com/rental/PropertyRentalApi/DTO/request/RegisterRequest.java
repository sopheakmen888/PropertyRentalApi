package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RegisterRequest {
    private String fullname;
    private String username;
<<<<<<< Updated upstream
=======
    private String username;
>>>>>>> Stashed changes
    private String email;
    private String password;
    private String phone;
    private List<Long> roles;
}
