package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.UploadsImages;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Enum.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadsImagesRepository extends JpaRepository<UploadsImages, Long> {

    Optional<UploadsImages> findByUserAndOwnerType(
            Users user,
            OwnerType ownerType
    );
}
