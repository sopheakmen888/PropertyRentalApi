package com.rental.PropertyRentalApi.Utils;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.badRequest;

@Component
@RequiredArgsConstructor
public class UserValidatorUtil {

    private final UserRepository userRepository;

    // =================
    // EMAIL FORMAT VALIDATOR
    // =================
    public void validateEmailFormat(String email) {
        if (email == null || !email.contains("@") || !email.endsWith(".com")) {
            throw badRequest("Email must contain '@' and end with '.com'");
        }
    }

    // =================
    // CREATE VALIDATION
    // =================
    public void validateCreate(UserCreateRequest request) {
        validateEmailFormat(request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw badRequest("Email is already in use.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw badRequest("Username is already in use.");
        }
    }

    // =================
    // UPDATE VALIDATION (PROFILE ONLY)
    // =================
    public void validateUpdate(UserUpdateRequest request) {

        if (request.getFullname() == null || request.getFullname().isBlank()) {
            throw badRequest("Full name must not be empty.");
        }

        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw badRequest("Phone number must not be empty.");
        }
    }
}
