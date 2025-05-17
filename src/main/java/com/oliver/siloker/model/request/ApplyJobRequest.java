package com.oliver.siloker.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplyJobRequest {

    @NotBlank(message = "job_id must be provided")
    private Long job_id;

    @NotBlank(message = "cv must be provided")
    private MultipartFile cv;
}
