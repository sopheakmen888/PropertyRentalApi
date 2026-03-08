package com.rental.PropertyRentalApi.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_profile")
@Data
public class UsersProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "urls")
    private String urls;

    @Column(name = "public_id")
    private String publicId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
}
