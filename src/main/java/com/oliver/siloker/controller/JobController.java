package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.entity.Job;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.request.CreateJobRequest;
import com.oliver.siloker.model.response.BaseResponse;
import com.oliver.siloker.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(ApiRoutes.JOB)
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping(
            path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<Boolean>> createJob(
            @ModelAttribute CreateJobRequest request
    ) throws IOException, ResourceNotFoundException {
        Job job = jobService.createJob(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<>(
                        HttpStatus.CREATED.value(),
                        "Success",
                        job != null
                ));
    }
}
