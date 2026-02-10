package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
@SuppressWarnings("unused")
public class PropertySeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding properties...");

        // ✅ Get a user to own seeded properties
        Users owner = userRepository.findByUsername("admin")
                .orElseThrow(() -> new RuntimeException("Admin user not found. Seed users first."));

        seedProperty(owner,
                "Cozy Studio Apartment",
                "Small but comfortable studio near campus",
                "Street 123, Phnom Penh",
                new BigDecimal("120"),
                new BigDecimal("10"),
                new BigDecimal("5"));

        seedProperty(owner,
                "Spacious 2 Bedroom Flat",
                "Perfect for roommates or family",
                "Street 456, Phnom Penh",
                new BigDecimal("250"),
                new BigDecimal("20"),
                new BigDecimal("10"));

        seedProperty(owner,
                "Modern Condo",
                "Luxury condo with pool and gym",
                "Street 789, Phnom Penh",
                new BigDecimal("400"),
                new BigDecimal("30"),
                new BigDecimal("15"));

        log.info("Property seeding completed.");
    }

    private void seedProperty(
            Users owner,
            String title,
            String description,
            String address,
            BigDecimal price,
            BigDecimal electricityCost,
            BigDecimal waterCost
    ) {

        if (propertyRepository.existsByTitle(title)) {
            log.info("Property already exists. Skipping.");
            return;
        }

        Properties property = new Properties();
        property.setTitle(title);
        property.setDescription(description);
        property.setAddress(address);
        property.setPrice(price);
        property.setElectricityCost(electricityCost);
        property.setWaterCost(waterCost);

        // 🔗 VERY IMPORTANT
        property.setCreatedBy(owner);

        propertyRepository.save(property);
        log.info("Seeded property '{}'", title);
    }
}
