package com.rental.PropertyRentalApi.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

//@EnableMethodSecurity
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // DEV: disable CSRF (API testing)
                // PROD: still disabled if using JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // ============================
                // CORS CONFIG
                // ============================
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // ============================
                    // DEV ORIGINS
                    // ============================
//                    config.addAllowedOrigin("http://localhost:3000");
//                    config.addAllowedOrigin("http://localhost:5173");

                    // FOR TESTING IN STAGING
//                    config.addAllowedOrigin("http://127.0.0.1:5500");
//                    config.addAllowedOrigin("http://127.0.0.1:3000");
                    config.addAllowedOriginPattern("*");

                    // ============================
                    // PROD ORIGINS (UNCOMMENT)
                    // ============================
                    // config.addAllowedOrigin("https://yourdomain.com");
                    // config.addAllowedOrigin("https://www.yourdomain.com");

                    // Allowed HTTP methods
                    String[] allowedMethods = {
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    };
                    for (String method : allowedMethods) {
                        config.addAllowedMethod(method);
                    }

                    // DEV: allow all headers
                    config.addAllowedHeader("*");
                    config.addExposedHeader("*");

                    // PROD: restrict headers
//                     config.addAllowedHeader("Authorization");
//                     config.addAllowedHeader("Content-Type");
//                     config.addExposedHeader("Authorization");

                    // Allow cookies / Authorization header
                    config.setAllowCredentials(true);

                    return config;
                }))

                // ============================
                // AUTHORIZATION RULES
                // ============================
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/uploads/**").permitAll()
//                                .requestMatchers("/api/users/profile").permitAll()

                                // ============================
                                // AUTHENTICATED ENDPOINT
                                // ============================
                                .requestMatchers("/api/reviews/**").authenticated()
                                .requestMatchers("/api/users/profile").authenticated()
                                .requestMatchers("/api/me").authenticated()
                                .requestMatchers("/api/properties/favorite/**").authenticated()

                        // ============================
                        // ROLE BASE AUTHORIZATION
                        // ============================
//                        .requestMatchers("/api/users/**").hasRole("admin")
                                .requestMatchers("/api/admin/**").hasRole("admin")
                        .requestMatchers("/api/properties/**").hasAnyRole("admin", "agent")

                // DEV: allow all endpoints
//                                .anyRequest().permitAll()

                // ============================
                // PROD (UNCOMMENT)
                // ============================
                         .anyRequest().authenticated()
                )

                // ============================
                // STATELESS SESSION
                // ============================
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ============================
                // EXCEPTION HANDLING
                // ============================
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // handle 401
                        .accessDeniedHandler(jwtAccessDeniedHandler) // handle 403
                );

        // PROD: add JWT filter here
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is safe for both DEV and PROD
        return new BCryptPasswordEncoder();
    }

}
