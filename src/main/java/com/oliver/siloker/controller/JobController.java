package com.oliver.siloker.controller;

import com.oliver.siloker.component.ApiRoutes;
import com.oliver.siloker.model.entity.job.Job;
import com.oliver.siloker.model.entity.job.JobApplication;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.request.CreateJobRequest;
import com.oliver.siloker.model.response.BaseResponse;
import com.oliver.siloker.model.response.JobApplicationResponse;
import com.oliver.siloker.model.response.JobResponse;
import com.oliver.siloker.model.response.PagingInfo;
import com.oliver.siloker.service.JobApplicationService;
import com.oliver.siloker.service.JobService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(ApiRoutes.JOB)
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobApplicationService jobApplicationService;

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

    @GetMapping
    public ResponseEntity<BaseResponse<PagingInfo<JobResponse>>> getJobs(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        PagingInfo<JobResponse> jobPagingInfo = jobService.getJobs(query, page, size);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        jobPagingInfo
                )
        );
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<BaseResponse<Boolean>> applyToJob(
            @PathVariable Long jobId
    ) throws ResourceNotFoundException {
        JobApplication jobApplication = jobApplicationService.applyToJob(jobId);
        return ResponseEntity.ok(new BaseResponse<>(
                HttpStatus.OK.value(),
                "Job application submitted",
                jobApplication != null
        ));
    }

    @GetMapping("/applicants")
    public ResponseEntity<BaseResponse<PagingInfo<JobApplicationResponse>>> getJobApplicants(
            @RequestParam(name = "job_id") Long jobId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) throws ResourceNotFoundException {
        PagingInfo<JobApplicationResponse> jobApplicationPagingInfo = jobApplicationService.getJobApplicants(jobId, page, size);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        jobApplicationPagingInfo
                )
        );
    }
}
