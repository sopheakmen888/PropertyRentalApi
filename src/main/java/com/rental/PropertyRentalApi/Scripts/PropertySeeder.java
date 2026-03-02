package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Categories;
import com.rental.PropertyRentalApi.Entity.Commune;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.CategoryRepository;
import com.rental.PropertyRentalApi.Repository.CommuneRepository;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(7)
public class PropertySeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommuneRepository communeRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding properties...");

        // ✅ Get a user to own seeded properties
        Users owner = userRepository.findByUsername("admin")
                .orElseThrow(() -> notFound("Admin user not found. Seed users first."));

        seedProperty(owner,
                "Cozy Studio Apartment",
                "Small but comfortable studio near campus",
                "Street 123, Phnom Penh",
                new BigDecimal("120"),
                new BigDecimal("10"),
                new BigDecimal("5"),
                "house",
                "Toul Tompoung");
                

        seedProperty(owner,
                "Spacious 2 Bedroom Flat",
                "Perfect for roommates or family",
                "Street 456, Phnom Penh",
                new BigDecimal("250"),
                new BigDecimal("20"),
                new BigDecimal("10"),
                "apartment",
                "Boeng Keng Kang II");

        seedProperty(owner,
                "Modern Condo",
                "Luxury condo with pool and gym",
                "Street 789, Phnom Penh",
                new BigDecimal("400"),
                new BigDecimal("30"),
                new BigDecimal("15"),
                "condo",
                "Boeng Keng Kang I");

        log.info("Property seeding completed.");
    }

    private void seedProperty(
            Users owner,
            String title,
            String description,
            String address,
            BigDecimal price,
            BigDecimal electricityCost,
            BigDecimal waterCost,
            String categoryName,
            String communeName
    ) {

        if (propertyRepository.existsByTitle(title)) {
            log.info("Property already exists. Skipping.");
            return;
        }

        Categories category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> notFound("Category '" + categoryName + "' not found. Seed categories first."));
        Commune commune = communeRepository.findByName(communeName)
                .orElseThrow(() -> notFound("Commune '" + communeName + "' not found."));

        Properties property = new Properties();
        property.setTitle(title);
        property.setDescription(description);
        property.setAddress(address);
        property.setPrice(price);
        property.setElectricityCost(electricityCost);
        property.setWaterCost(waterCost);

        // 🔗 VERY IMPORTANT
        property.setCreatedBy(owner);
        property.setCategory(category);
        property.setCommune(commune);

        propertyRepository.save(property);
        log.info("Seeded property '{}'", title);
    }
}