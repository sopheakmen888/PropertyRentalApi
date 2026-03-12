package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.RoleRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(8)
public class AgentSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Seeding agents...");

        seedAgents();

        log.info("Agent seeding completed");
    }

    private void seedAgents() {

        Roles agentRole = roleRepository.findByName("agent")
                .orElseThrow(() -> {
                    log.error("Failed to seed agents!");
                    return notFound("Role agent not found.");
                });

        List<Users> agents = List.of(
                Users.builder()
                        .fullname("John Carter")
                        .username("john_agent")
                        .email("john.agent@gmail.com")
                        .password(passwordEncoder.encode("John@123"))
                        .phone("097 615 204")
                        .roles(new HashSet<>(Set.of(agentRole)))
                        .enabled(true)
                        .build(),

                Users.builder()
                        .fullname("Emily Stone")
                        .username("emily_agent")
                        .email("emily.agent@gmail.com")
                        .password(passwordEncoder.encode("Emily@123"))
                        .phone("088 349 572")
                        .roles(new HashSet<>(Set.of(agentRole)))
                        .enabled(true)
                        .build(),

                Users.builder()
                        .fullname("Michael Tan")
                        .username("michael_agent")
                        .email("michael.agent@gmail.com")
                        .password(passwordEncoder.encode("Michael@123"))
                        .phone("071 926 438")
                        .roles(new HashSet<>(Set.of(agentRole)))
                        .enabled(true)
                        .build(),

                Users.builder()
                        .fullname("Sarah Kim")
                        .username("sarah_agent")
                        .email("sarah.agent@gmail.com")
                        .password(passwordEncoder.encode("Sarah@123"))
                        .phone("089 754 163")
                        .roles(new HashSet<>(Set.of(agentRole)))
                        .enabled(true)
                        .build(),

                Users.builder()
                        .fullname("David Lee")
                        .username("david_agent")
                        .email("david.agent@gmail.com")
                        .password(passwordEncoder.encode("David@123"))
                        .phone("012 638 905")
                        .roles(new HashSet<>(Set.of(agentRole)))
                        .enabled(true)
                        .build()
        );

        for (Users agent : agents) {

            if (userRepository.existsByEmail(agent.getEmail())) {
                log.info("Agent {} already exists, skipping...", agent.getEmail());
                continue;
            }

            userRepository.save(agent);
            log.info("Seeded agent: {}", agent.getEmail());
        }
    }
}