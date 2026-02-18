package com.rental.PropertyRentalApi.Controller;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam("images") List<MultipartFile> images
    ) throws IOException, java.io.IOException {

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : images) {

            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body("File too large. Max 5MB allowed.");
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String UPLOAD_DIR = "uploads/";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "http://localhost:8000/api/uploads/" + fileName;
            imageUrls.add(fileUrl);
        }

        return ResponseEntity.ok(imageUrls);
    }
}
