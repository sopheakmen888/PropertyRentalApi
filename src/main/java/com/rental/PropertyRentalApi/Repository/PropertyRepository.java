package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Properties, Long> {

    boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"images"})
    Page<Properties> findAll(Pageable pageable);


    // Find properties created by a specific user
    List<Properties> findAllByCreatedBy(Users user);

    @Query("SELECT p FROM Properties p WHERE " +
           "(:provinceId IS NULL OR p.commune.district.province.id = :provinceId) AND " +
           "(:districtId IS NULL OR p.commune.district.id = :districtId) AND " +
           "(:communeId IS NULL OR p.commune.id = :communeId)")
    List<Properties> filterProperties(
        @Param("provinceId") Long provinceId,
        @Param("districtId") Long districtId,
        @Param("communeId") Long communeId
);
}
