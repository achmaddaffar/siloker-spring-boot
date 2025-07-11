package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class JobDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;

    private String fullName;
    private String phoneNumber;
    private String bio;
    private String profilePictureUrl;
    private EmployerResponse employer;
    private Boolean isApplicable;

    private String createdAt;
    private String updatedAt;
}
