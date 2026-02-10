package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
    boolean existsByName(String name);
}
