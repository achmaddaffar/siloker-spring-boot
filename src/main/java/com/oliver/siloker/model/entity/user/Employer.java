package com.oliver.siloker.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "employers")
@Entity
@Data
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "company_name")
    private String companyName;

    @Column(nullable = false, name = "position")
    private String position;

    @Column(nullable = false, name = "company_website")
    private String companyWebsite;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
