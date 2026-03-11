package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Properties, Long>,
        JpaSpecificationExecutor<Properties> {

    boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"images", "createdBy", "category"})
    Page<Properties> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"images", "createdBy", "category"})
    Optional<Properties> findById(Long id);


    // Find properties created by a specific user
    @EntityGraph(attributePaths = {"images", "createdBy", "category"})
    List<Properties> findAllByCreatedBy(Users user);
}
