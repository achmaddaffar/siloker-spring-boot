package com.oliver.siloker.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterJobSeekerRequest {

    @NotBlank(message = "resume_url must be provided")
    private String resumeUrl;

    private List<String> skills;

    private List<String> experiences;
}
