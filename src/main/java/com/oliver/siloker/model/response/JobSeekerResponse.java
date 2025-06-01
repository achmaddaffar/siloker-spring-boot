package com.oliver.siloker.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oliver.siloker.model.entity.user.Experience;
import com.oliver.siloker.model.entity.user.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class JobSeekerResponse {

    private Long id;
    private String resumeUrl;
    private List<Skill> skills;
    private List<Experience> experiences;
}
