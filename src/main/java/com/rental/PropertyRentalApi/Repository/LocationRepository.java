package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Long> {
}
