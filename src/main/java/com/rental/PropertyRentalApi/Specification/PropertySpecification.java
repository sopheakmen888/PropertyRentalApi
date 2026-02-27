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
            String propertyType
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"));
            }

            if (description != null && !description.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("description")),
                                "%" + description.toLowerCase() + "%"));
            }

            if (address != null && !address.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"));
            }

            if (propertyType != null && !propertyType.isBlank()) {
                predicates.add(
                        cb.equal(root.get("propertyType"), propertyType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}