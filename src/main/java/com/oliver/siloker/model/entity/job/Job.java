package com.oliver.siloker.model.entity.job;

import com.oliver.siloker.model.entity.user.Employer;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "jobs")
@Entity
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @Column(nullable = false, name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
