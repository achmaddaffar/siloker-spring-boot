package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class GetUserResponse {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String bio;
    private Long employerId;
    private Long jobSeekerId;
    private String profilePictureUrl;
}
