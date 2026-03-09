package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictsRepository extends JpaRepository<Districts, Long> {

    List<Districts> findByProvinceId(Long provinceId);

    boolean existsByName(String name);
}
