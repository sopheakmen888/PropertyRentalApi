package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommuneRepository extends JpaRepository<Commune, Long> {

    List<Commune> findByDistrictsId(Long districtId);

    Optional<Commune> findByName(String name);

    boolean existsByName(String name);
}
