package com.oliver.siloker.service;

import com.oliver.siloker.component.FileUtils;
import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.custom.CustomUserDetails;
import com.oliver.siloker.model.entity.job.Job;
import com.oliver.siloker.model.entity.user.Employer;
import com.oliver.siloker.model.entity.user.User;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.EmployerRepository;
import com.oliver.siloker.model.repository.JobApplicationRepository;
import com.oliver.siloker.model.repository.JobRepository;
import com.oliver.siloker.model.repository.UserRepository;
import com.oliver.siloker.model.request.CreateJobRequest;
import com.oliver.siloker.model.response.JobDetailResponse;
import com.oliver.siloker.model.response.JobResponse;
import com.oliver.siloker.model.response.PagingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JwtUtils jwtUtils;
    private final FileUtils fileUtils;

    private final String uploadDir = "/uploads/images/";

    public Job createJob(
            CreateJobRequest request
    ) throws ResourceNotFoundException, IOException {
        CustomUserDetails userDetails = jwtUtils.getUserDetails();
        User user = userRepository.findByPhoneNumber(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employer employer = employerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        Path uploadPath = FileUtils.validateFilename(request.getImage().getOriginalFilename(), uploadDir);

        String filename = UUID.randomUUID() + "_" + request.getImage().getOriginalFilename();
        File dest = new File(uploadPath.toFile(), filename);
        try {
            request.getImage().transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image");
        }

        String imageUrl = "/images/" + filename;

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setImageUrl(imageUrl);
        job.setEmployer(employer);
        job.setCreatedAt(LocalDateTime.now().toString());

        return jobRepository.save(job);
    }

    public PagingInfo<JobResponse> getJobs(
            String query,
            Long employerId,
            Integer pageNumber,
            Integer pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Job> jobs = jobRepository.filter(query, employerId, pageRequest);

        return PagingInfo.convertFromPage(jobs.map((item) -> new JobResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getImageUrl(),
                item.getEmployer().getId(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        )));
    }

    public JobDetailResponse getJob(
            Long jobId
    ) throws ResourceNotFoundException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job is not found"));

        User user = userRepository.findById(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User is not found"));
        Boolean isApplicable = Objects.nonNull(user.getEmployer()) &&
                !Objects.equals(user.getEmployer().getId(), job.getEmployer().getId()) &&
                !jobApplicationRepository.existsByJobSeekerAndJob(user.getJobSeeker(), job);

        return new JobDetailResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getImageUrl(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getEmployer().toResponse(),
                isApplicable,
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
