package com.rental.PropertyRentalApi.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","property_id"}))
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Properties property;
}
