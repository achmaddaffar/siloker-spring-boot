package com.oliver.siloker.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateJobRequest {

    @NotBlank(message = "title must be provided")
    private String title;

    @NotBlank(message = "description must be provided")
    private String description;

    @NotBlank(message = "image must be provided")
    private MultipartFile image;
}
