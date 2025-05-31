package com.oliver.siloker.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterRequest {

    @NotBlank(message = "full_name must be provided")
    private String full_name;

    @NotBlank(message = "phone_number must be provided")
    private String phone_number;

    @NotBlank(message = "password must be provided")
    private String password;

    private String bio;

    @NotBlank(message = "password must be provided")
    private MultipartFile profile_picture;
}
