package com.oliver.siloker.service;

import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.custom.CustomUserDetails;
import com.oliver.siloker.model.entity.job.ApplicationStatus;
import com.oliver.siloker.model.entity.job.Job;
import com.oliver.siloker.model.entity.job.JobApplication;
import com.oliver.siloker.model.entity.user.JobSeeker;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.JobApplicationRepository;
import com.oliver.siloker.model.repository.JobRepository;
import com.oliver.siloker.model.repository.UserRepository;
import com.oliver.siloker.model.response.JobApplicationResponse;
import com.oliver.siloker.model.response.PagingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public JobApplication applyToJob(Long jobId) throws ResourceNotFoundException {
        CustomUserDetails customUserDetails = jwtUtils.getUserDetails();
        User user = userRepository.findByPhoneNumber(customUserDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobSeeker jobSeeker = user.getJobSeeker();
        if (jobSeeker == null)
            throw new IllegalStateException("User is not registered as job seeker");

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job is not found"));

        if (jobApplicationRepository.existsByJobSeekerAndJob(jobSeeker, job))
            throw new IllegalStateException("You already applied to this job");

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJobSeeker(jobSeeker);
        jobApplication.setJob(job);
        jobApplication.setStatus(ApplicationStatus.PENDING);
        jobApplication.setCreatedAt(LocalDateTime.now().toString());

        return jobApplicationRepository.save(jobApplication);
    }

    public PagingInfo<JobApplicationResponse> getJobApplicants(
            Long jobId,
            Integer page,
            Integer size
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getEmployer() == null)
            throw new IllegalStateException("User is not registered as Employer");

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.filterByJobId(jobId, pageRequest);
        return PagingInfo.convertFromPage(jobApplicationPage.map(JobApplication::toResponse));
    }

    public JobApplicationResponse changeApplicantStatus(
            Long jobApplicationId,
            ApplicationStatus applicationStatus
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getEmployer() == null)
            throw new IllegalStateException("User is not registered as Employer");

        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application is not exist"));

        if (!Objects.equals(jobApplication.getJob().getEmployer().getId(), user.getEmployer().getId()))
            throw new IllegalStateException("You are not the employer");

        jobApplication.setStatus(applicationStatus);
        jobApplication.setUpdatedAt(LocalDateTime.now().toString());

        return jobApplicationRepository
                .save(jobApplication)
                .toResponse();
    }
}
