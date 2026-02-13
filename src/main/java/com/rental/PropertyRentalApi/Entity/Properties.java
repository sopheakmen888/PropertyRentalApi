package com.rental.PropertyRentalApi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "properties")
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "electricity_cost", precision = 10, scale = 2)
    private BigDecimal electricityCost;

    @Column(name = "water_cost", precision = 10, scale = 2)
    private BigDecimal waterCost;

    ////////////////////////////////////////////////////
    // CREATED BY USER
    ////////////////////////////////////////////////////

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;

    ////////////////////////////////////////////////////
    // CATEGORY
    ////////////////////////////////////////////////////
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name")
    private Categories categoryName;

    ////////////////////////////////////////////////////
    // PROPERTY IMAGES (ONE PROPERTY → MANY IMAGES)
    ////////////////////////////////////////////////////

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyImages> images;

    ////////////////////////////////////////////////////
    // FAVORITES
    ////////////////////////////////////////////////////

    @OneToMany(mappedBy = "property")
    private List<Favorites> favorites;

    ////////////////////////////////////////////////////
    // TIMESTAMP
    ////////////////////////////////////////////////////

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    ////////////////////////////////////////////////////
    // AUTO TIMESTAMP
    ////////////////////////////////////////////////////

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
