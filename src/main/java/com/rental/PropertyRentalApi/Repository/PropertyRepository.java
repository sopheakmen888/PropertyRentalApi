package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.DTO.response.PropertyResponse;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
    SELECT new com.rental.PropertyRentalApi.DTO.response.PropertyResponse(
        p.id,
        p.title,
        p.description,
        p.address,
        p.price,
        p.electricityCost,
        p.waterCost,
        p.available,
        COUNT(DISTINCT r.id),
        COUNT(DISTINCT f.id)
    )
    FROM Properties p
    LEFT JOIN Reviews r ON r.property = p
    LEFT JOIN Favorites f ON f.property = p
    GROUP BY p.id
    """)
    Page<PropertyResponse> findAllWithCounts(Pageable pageable);

}
