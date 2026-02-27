package com.rental.PropertyRentalApi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rental.PropertyRentalApi.Entity.Commune;

public interface CommuneRepository extends JpaRepository<Commune, Long> {
    Optional<Commune> findByName(String name);

}
