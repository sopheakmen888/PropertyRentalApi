package com.rental.PropertyRentalApi.Service;

import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {

    List<String> uploadPropertyImages(Long propertyId, List<MultipartFile> files);

    String uploadUserProfile(Long userId, MultipartFile file);

    void deleteImage(Long imageId);
}
