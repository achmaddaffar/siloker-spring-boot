package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oliver.siloker.model.entity.user.Experience;
import com.oliver.siloker.model.entity.user.Skill;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobSeekerResponse {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String bio;
    private String resumeUrl;
    private List<Skill> skills;
    private List<Experience> experiences;
}
