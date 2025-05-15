package com.oliver.siloker.model.entity.job;

import com.oliver.siloker.model.entity.user.JobSeeker;
import com.oliver.siloker.model.response.JobApplicationResponse;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "job_applications")
@Entity
@Data
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_seeker_id")
    private JobSeeker jobSeeker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    public JobApplicationResponse toResponse() {
        return new JobApplicationResponse(
                getId(),
                getJobSeeker().getId(),
                getJobSeeker().getUser().getFullName(),
                getJobSeeker().getUser().getPhoneNumber(),
                getJobSeeker().getUser().getBio(),
                getJobSeeker().getResumeUrl(),
                getJobSeeker().getSkills(),
                getJobSeeker().getExperiences(),
                getStatus(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}