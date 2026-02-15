package com.rental.PropertyRentalApi.Utils;

import com.rental.PropertyRentalApi.DTO.request.UserCreateRequest;
import com.rental.PropertyRentalApi.DTO.request.UserUpdateRequest;
import com.rental.PropertyRentalApi.Entity.Categories;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.CategoryRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class HelperFunction {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final JwtService jwtService;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String UPLOAD_DIR = "uploads/profile-photos";

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

    // =================
    // GET CATEGORY
    // =================
    public Categories getCategoryOrThrow(String category) {
        return categoryRepository.findByName(category)
                .orElseThrow(() -> notFound("Category not found."));
    }

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

    public String processBase64Image(String base64Image, String oldPhotoPath) {

        // ======================
        // SKIP CASE
        // ======================
        if (base64Image == null || base64Image.isBlank()) {
            return null;
        }

        try {

            if (!base64Image.contains(",")) {
                throw new RuntimeException("Invalid Base64 format");
            }

            String[] parts = base64Image.split(",", 2);
            String metaData = parts[0];     // data:image/png;base64
            String base64Data = parts[1];

            if (!metaData.startsWith("data:image/")) {
                throw new RuntimeException("Only image files allowed");
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // ======================
            // VALIDATE SIZE
            // ======================
            if (imageBytes.length > MAX_FILE_SIZE) {
                throw new RuntimeException("Image exceeds 5MB limit");
            }

            // ======================
            // VALIDATE EXTENSION
            // ======================
            String extension = metaData.substring(
                    metaData.indexOf("/") + 1,
                    metaData.indexOf(";")
            ).toLowerCase();

            if (!List.of("png", "jpg", "jpeg", "webp").contains(extension)) {
                throw new RuntimeException("Unsupported image type");
            }

            // ======================
            // CREATE DIRECTORY
            // ======================
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ======================
            // DELETE OLD FILE
            // ======================
            if (oldPhotoPath != null) {
                Files.deleteIfExists(Paths.get(oldPhotoPath));
            }

            // ======================
            // SAVE NEW FILE
            // ======================
            String fileName = UUID.randomUUID() + "." + extension;
            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, imageBytes);

            // Return relative path (cleaner for frontend)
            return "/uploads/profile/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}
