package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.PropertyImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyImagesRepository extends JpaRepository<PropertyImages, Long> {
    List<PropertyImages> findByPropertyId(Long propertyId);
}
