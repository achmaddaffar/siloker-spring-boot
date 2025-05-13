package com.oliver.siloker.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    @NotBlank(message = "full_name must be provided")
    private String fullName;

    @NotBlank(message = "phone_number must be provided")
    private String phoneNumber;

    @NotBlank(message = "password must be provided")
    private String password;

    private String bio;
}
