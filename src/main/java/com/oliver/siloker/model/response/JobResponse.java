package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long employerId;
    private String createdAt;
    private String updatedAt;
}
