package com.oliver.siloker.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterEmployerRequest {

    @NotBlank(message = "company_name must be provided")
    private String companyName;

    @NotBlank(message = "position must be provided")
    private String position;

    @NotBlank(message = "company_website must be provided")
    private String companyWebsite;
}
