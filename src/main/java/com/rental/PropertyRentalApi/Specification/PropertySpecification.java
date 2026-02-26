package com.rental.PropertyRentalApi.Specification;

import com.rental.PropertyRentalApi.Entity.Properties;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Properties> search(
            String title,
            String description,
            String categoryName,
            String address,
<<<<<<< HEAD
            String propertyType,
            Long provinceId,
            Long districtId,
            Long communeId
=======
            String propertyType
>>>>>>> 2887ad5 (fix search feature and add update authenticated user profile)
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

<<<<<<< HEAD
            // ======================
            // TEXT SEARCH
            // ======================
            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
=======
            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"));
>>>>>>> 2887ad5 (fix search feature and add update authenticated user profile)
            }

            if (description != null && !description.isBlank()) {
                predicates.add(
<<<<<<< HEAD
                        cb.like(
                                cb.lower(root.get("description")),
                                "%" + description.toLowerCase() + "%"
                        )
                );
            }

            if (categoryName != null && !categoryName.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("categoryName")),
                                "%" + categoryName.toLowerCase() + "%"
                        )
                );
=======
                        cb.like(cb.lower(root.get("description")),
                                "%" + description.toLowerCase() + "%"));
>>>>>>> 2887ad5 (fix search feature and add update authenticated user profile)
            }

            if (address != null && !address.isBlank()) {
                predicates.add(
<<<<<<< HEAD
                        cb.like(
                                cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"
                        )
                );
=======
                        cb.like(cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"));
>>>>>>> 2887ad5 (fix search feature and add update authenticated user profile)
            }

            if (propertyType != null && !propertyType.isBlank()) {
                predicates.add(
<<<<<<< HEAD
                        cb.equal(root.get("propertyType"), propertyType)
                );
            }

            // ======================
            // LOCATION FILTER
            // ======================
            if (provinceId != null) {
                predicates.add(
                        cb.equal(
                                root.get("commune")
                                        .get("district")
                                        .get("province")
                                        .get("id"),
                                provinceId
                        )
                );
            }

            if (districtId != null) {
                predicates.add(
                        cb.equal(
                                root.get("commune")
                                        .get("province")
                                        .get("id"),
                                districtId
                        )
                );
            }

            if (communeId != null) {
                predicates.add(
                        cb.equal(
                                root.get("commune")
                                        .get("id"),
                                communeId
                        )
                );
=======
                        cb.equal(root.get("propertyType"), propertyType));
>>>>>>> 2887ad5 (fix search feature and add update authenticated user profile)
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}