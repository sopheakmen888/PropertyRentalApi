package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Provinces;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Provinces, Long> {

    boolean existsByName(String name);
}
