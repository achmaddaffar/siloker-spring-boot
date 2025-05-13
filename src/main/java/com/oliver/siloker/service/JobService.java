package com.oliver.siloker.service;

import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.entity.Job;
import com.oliver.siloker.model.entity.user.Employer;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.EmployerRepository;
import com.oliver.siloker.model.repository.JobRepository;
import com.oliver.siloker.model.request.CreateJobRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final JwtUtils jwtUtils;

    private final String uploadDir = "/uploads/images/";

    public Job createJob(
            CreateJobRequest request
    ) throws ResourceNotFoundException, IOException {
        Employer employer = employerRepository.findByUserId(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
        if (originalFilename.contains(".."))
            throw new SecurityException("Filename contains invalid path sequence: " + originalFilename);

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path destinationFile = uploadPath.resolve(originalFilename).normalize();
        if (!destinationFile.startsWith(uploadPath))
            throw new SecurityException("Cannot store file outside the designated directory.");

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
}
