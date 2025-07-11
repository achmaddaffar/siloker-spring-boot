package com.oliver.siloker.service;

import com.oliver.siloker.component.FileUtils;
import com.oliver.siloker.component.JwtUtils;
import com.oliver.siloker.model.entity.user.*;
import com.oliver.siloker.model.exception.ResourceNotFoundException;
import com.oliver.siloker.model.repository.*;
import com.oliver.siloker.model.request.RegisterEmployerRequest;
import com.oliver.siloker.model.request.RegisterJobSeekerRequest;
import com.oliver.siloker.model.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final JwtUtils jwtUtils;

    private final String uploadDir = "/uploads/images/";

    public User getUser() throws ResourceNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String phoneNumber = userDetails.getUsername();

        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(
            UpdateUserRequest request
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFullName(request.getFullName());
        user.setBio(request.getBio());

        return userRepository.save(user);
    }

    public Boolean uploadProfilePicture(
            MultipartFile profilePicture
    ) throws ResourceNotFoundException, IOException {
        User user = userRepository.findById(jwtUtils.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getProfilePictureUrl() != null) {
            String oldFilename = user.getProfilePictureUrl().replace("/images/", "");
            File oldFile = new File(uploadDir, oldFilename);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        Path uploadPath = FileUtils.validateFilename(profilePicture.getOriginalFilename(), uploadDir);
        String filename = UUID.randomUUID() + "_" + profilePicture.getOriginalFilename();
        File destination = new File(uploadPath.toFile(), filename);
        try {
            profilePicture.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image");
        }
        String imageUrl = "/images/" + filename;

        user.setProfilePictureUrl(imageUrl);
        userRepository.save(user);

        return true;
    }

//    public JobSeeker registerJobSeeker(
//            RegisterJobSeekerRequest request
//    ) throws Exception {
//        try {
//            User user = userRepository.findByPhoneNumber(jwtUtils.getUserDetails().getUsername())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//            JobSeeker jobSeeker = user.getJobSeeker() == null ? new JobSeeker() : jobSeekerRepository.findByUserId(user.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Job Seeker not found"));
//
//            jobSeeker.setUser(user);
//            jobSeeker.setResumeUrl(request.getResumeUrl());
//            jobSeeker.setCreatedAt(LocalDateTime.now().toString());
//
//            skillRepository.deleteAll(Optional.ofNullable(jobSeeker.getSkills()).orElse(new ArrayList<>()));
//            experienceRepository.deleteAll(Optional.ofNullable(jobSeeker.getExperiences()).orElse(new ArrayList<>()));
//
//            List<Skill> skills = request.getSkills().stream().map((skillName) -> {
//                Skill skill = new Skill();
//                skill.setName(skillName);
//                skill.setCreatedAt(LocalDateTime.now().toString());
//                return skill;
//            }).toList();
//
//            List<Experience> experiences = request.getExperiences().stream().map((experienceName) -> {
//                Experience experience = new Experience();
//                experience.setName(experienceName);
//                experience.setCreatedAt(LocalDateTime.now().toString());
//                return experience;
//            }).toList();
//
//            jobSeeker.getSkills().clear();
//            jobSeeker.getSkills().addAll(skills);
//            jobSeeker.getExperiences().clear();
//            jobSeeker.getExperiences().addAll(experiences);
//            user.setJobSeeker(jobSeeker);
//
//            return jobSeekerRepository.save(jobSeeker);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }

    public JobSeeker registerJobSeeker(RegisterJobSeekerRequest request) throws ResourceNotFoundException {
        User user = userRepository.findByPhoneNumber(jwtUtils.getUserDetails().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobSeeker jobSeeker = user.getJobSeeker() == null ? new JobSeeker() :
                jobSeekerRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Job Seeker not found"));

        jobSeeker.setUser(user);
        jobSeeker.setResumeUrl(request.getResumeUrl());
        jobSeeker.setCreatedAt(LocalDateTime.now().toString());

        // --- Skills merge logic ---
        List<Skill> currentSkills = Optional.ofNullable(jobSeeker.getSkills()).orElse(new ArrayList<>());
        List<String> newSkillNames = request.getSkills();

        // Remove skills no longer in request
        currentSkills.removeIf(skill -> !newSkillNames.contains(skill.getName()));

        // Add or update skills from request
        for (String skillName : newSkillNames) {
            boolean exists = currentSkills.stream().anyMatch(skill -> skill.getName().equals(skillName));
            if (!exists) {
                Skill skill = new Skill();
                skill.setName(skillName);
                skill.setCreatedAt(LocalDateTime.now().toString());
                currentSkills.add(skill);
            }
        }
        jobSeeker.setSkills(currentSkills);

        // --- Experiences merge logic ---
        List<Experience> currentExperiences = Optional.ofNullable(jobSeeker.getExperiences()).orElse(new ArrayList<>());
        List<String> newExperienceNames = request.getExperiences();

        currentExperiences.removeIf(exp -> !newExperienceNames.contains(exp.getName()));

        for (String expName : newExperienceNames) {
            boolean exists = currentExperiences.stream().anyMatch(exp -> exp.getName().equals(expName));
            if (!exists) {
                Experience experience = new Experience();
                experience.setName(expName);
                experience.setCreatedAt(LocalDateTime.now().toString());
                currentExperiences.add(experience);
            }
        }
        jobSeeker.setExperiences(currentExperiences);

        user.setJobSeeker(jobSeeker);

        return jobSeekerRepository.save(jobSeeker);
    }

    public Employer registerEmployer(
            RegisterEmployerRequest request
    ) throws ResourceNotFoundException {
        User user = userRepository.findByPhoneNumber(jwtUtils.getUserDetails().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employer employer = user.getEmployer() == null ? new Employer() : employerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        employer.setUser(user);
        employer.setCompanyName(request.getCompanyName());
        employer.setPosition(request.getPosition());
        employer.setCompanyWebsite(request.getCompanyWebsite());
        employer.setCreatedAt(LocalDateTime.now().toString());

        user.setEmployer(employer);

        return employerRepository.save(employer);
    }
}
