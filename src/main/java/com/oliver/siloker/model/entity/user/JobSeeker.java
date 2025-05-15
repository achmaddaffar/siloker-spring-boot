package com.oliver.siloker.model.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "job_seekers")
@Entity
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "resume_url")
    private String resumeUrl;

    @OneToMany
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private List<Skill> skills;

    @OneToMany
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private List<Experience> experiences;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
