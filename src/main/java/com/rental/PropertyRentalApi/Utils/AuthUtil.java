package com.rental.PropertyRentalApi.Utils;

import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.unauthorized;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtService jwtService;

    // =================
    // GET AUTHENTICATED USER
    // =================
    public Users getAuthenticatedUser() {
        Users currentUser = jwtService.getCurrentUser();

        if (currentUser == null) {
            throw unauthorized("Authentication required - no authenticated user found");
        }

        return currentUser;
    }
}
