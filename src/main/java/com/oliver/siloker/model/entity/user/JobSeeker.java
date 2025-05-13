package com.oliver.siloker.model.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "job_seekers")
@Entity
@Data
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
