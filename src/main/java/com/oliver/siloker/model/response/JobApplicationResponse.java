package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oliver.siloker.model.entity.job.ApplicationStatus;
import com.oliver.siloker.model.entity.user.Experience;
import com.oliver.siloker.model.entity.user.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class JobApplicationResponse {

    private Long id;
    private Long jobSeekerId;
    private String fullName;
    private String phoneNumber;
    private String bio;
    private String profilePictureUrl;
    private String resumeUrl;
    private String cvUrl;
    private List<Skill> skills;
    private List<Experience> experiences;
    private ApplicationStatus status;
    private Long jobId;
    private String title;
    private String description;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;
}
