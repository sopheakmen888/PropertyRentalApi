package com.rental.PropertyRentalApi.Entity;

import com.rental.PropertyRentalApi.Enum.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "uploads_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadsImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "urls")
    private String urls;

    @Column(name = "public_id")
    private String publicId;

    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Properties property;

    @OneToOne(fetch = FetchType.LAZY)    @JoinColumn(name = "user_id", unique = true)
    private Users user;
}
