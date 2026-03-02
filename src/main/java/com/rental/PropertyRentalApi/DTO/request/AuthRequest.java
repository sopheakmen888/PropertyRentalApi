package com.rental.PropertyRentalApi.DTO.request;

import lombok.Data;

@Data
public class AuthRequest {

//    email or username login support
        private String email_or_username;
        private String password;
}

