package com.oliver.siloker.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "full_name is required")
    private String full_name;

    @NotBlank(message = "phone_number is required")
    private String phone_number;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "bio is required")
    private String bio;

    @NotBlank(message = "profile_picture is required")
    private MultipartFile profile_picture;
}
