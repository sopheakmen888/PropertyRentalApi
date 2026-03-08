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
            String propertyType,
            Long provinceId,
            Long districtId,
            Long communeId,
            Boolean available
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

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
            }

            if (description != null && !description.isBlank()) {
                predicates.add(
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
            }

            if (address != null && !address.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"
                        )
                );
            }

            if (propertyType != null && !propertyType.isBlank()) {
                predicates.add(
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
            }

            if (available != null) {
                predicates.add(
                        cb.equal(root.get("available"), available)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}