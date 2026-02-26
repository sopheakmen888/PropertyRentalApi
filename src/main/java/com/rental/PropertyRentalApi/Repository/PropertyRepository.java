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

@Repository
public interface PropertyRepository extends JpaRepository<Properties, Long>,JpaSpecificationExecutor<Properties> {

    boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"images"})
    Page<Properties> findAll(Pageable pageable);


    // Find properties created by a specific user
    List<Properties> findAllByCreatedBy(Users user);

<<<<<<< HEAD
//    Page<Properties> searchProperties(Object object, Object object2, Object object3, Object object4, Object object5,
//            Pageable pageable);

=======
    Page<Properties> searchProperties(Object object, Object object2, Object object3, Object object4, Object object5,
            Pageable pageable);
>>>>>>> c471626 (serchpropery)
}
